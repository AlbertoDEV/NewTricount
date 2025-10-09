document.addEventListener('DOMContentLoaded', function() {
    const menuToggle = document.getElementById('menu-toggle');
    const languageMenu = document.getElementById('language-menu');

    if (menuToggle) {
        menuToggle.addEventListener('click', function() {
            languageMenu.classList.toggle('show');
        });
    }

    // Close the menu if clicking outside of it
    window.addEventListener('click', function(e) {
        if (languageMenu && !languageMenu.contains(e.target) && !menuToggle.contains(e.target)) {
            languageMenu.classList.remove('show');
        }
    });
});
