/**
 * Zamyka modal o podanym ID.
 * @param {string} modalId - ID elementu modala (np. 'welcomeUserModal').
 */
function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.remove('show');
        // Opcjonalnie: można dodać setTimeout, aby usunąć go z DOM po zakończeniu animacji,
        // ale zwykle ukrycie przez CSS (opacity: 0, visibility: hidden) wystarczy.
    }
}

/**
 * Pokazuje modal o podanym ID i ustawia automatyczne zamykanie.
 * @param {string} modalId - ID elementu modala.
 */
function showModal(modalId) {
    const modal = document.getElementById(modalId);
    if (!modal) return;

    // Pokaż modal z lekkim opóźnieniem, aby animacja zadziałała
    setTimeout(() => modal.classList.add('show'), 10);

    // Automatyczne zamykanie modala po 5 sekundach
    const timeoutId = setTimeout(() => closeModal(modalId), 5000);

    // Handler do zamykania modala klawiszem Escape
    const keydownHandler = (e) => {
        if (e.key === 'Escape' && modal.classList.contains('show')) {
            cleanupAndClose();
        }
    };

    // Funkcja czyszcząca, aby uniknąć wycieków pamięci i wielokrotnego dodawania listenerów
    function cleanupAndClose() {
        clearTimeout(timeoutId); // Anuluj automatyczne zamykanie
        closeModal(modalId);
        document.removeEventListener('keydown', keydownHandler);
    }

    // Dodaj listener na klawisz Escape
    document.addEventListener('keydown', keydownHandler);

    // Przypisz eventy do przycisków wewnątrz modala
    // Używamy querySelector, aby znaleźć przyciski w kontekście konkretnego modala
    modal.querySelector('.modal-close')?.addEventListener('click', cleanupAndClose);
    modal.querySelector('.modal-button-primary')?.addEventListener('click', cleanupAndClose);

    // Zamykanie po kliknięciu na tło (overlay)
    modal.addEventListener('click', (e) => {
        if (e.target === modal) {
            cleanupAndClose();
        }
    });
}

// Ten kod uruchomi się, gdy cały dokument HTML zostanie załadowany.
// Wywołujemy skrypty warunkowe, które zostały osadzone w HTML.
document.addEventListener('DOMContentLoaded', function() {
    // Sprawdź, czy istnieje globalna zmienna (ustawiona przez skrypt inline) do pokazania modala
    if (window.showWelcomeModal) {
        showModal('welcomeUserModal');
    }
    if (window.showErrorModal) {
        showModal('errorModal');
    }
});