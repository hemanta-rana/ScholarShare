(function () {
    'use strict';

    /* ── Hamburger / sidebar toggle ── */
    const menuBtn = document.getElementById('menuBtn');
    const sidebar = document.querySelector('.sidebar');
    const overlay = document.getElementById('sidebarOverlay');

    function openSidebar() {
        if (sidebar) sidebar.classList.add('open');
        if (overlay) overlay.classList.add('open');
    }

    function closeSidebar() {
        if (sidebar) sidebar.classList.remove('open');
        if (overlay) overlay.classList.remove('open');
    }

    if (menuBtn) {
        menuBtn.addEventListener('click', function () {
            sidebar && sidebar.classList.contains('open') ? closeSidebar() : openSidebar();
        });
    }

    if (overlay) {
        overlay.addEventListener('click', closeSidebar);
    }

    /* Close sidebar when a nav item is clicked on mobile */
    document.querySelectorAll('.nav-item').forEach(function (item) {
        item.addEventListener('click', function () {
            if (window.innerWidth <= 768) closeSidebar();
        });
    });

    /* ── Admin profile dropdown ── */
    const adminChip     = document.getElementById('adminProfileChip');
    const adminDropdown = document.getElementById('adminProfileDropdown');

    function openAdminDropdown() {
        if (!adminDropdown) return;
        adminDropdown.hidden = false;
        if (adminChip) adminChip.setAttribute('aria-expanded', 'true');
    }

    function closeAdminDropdown() {
        if (!adminDropdown) return;
        adminDropdown.hidden = true;
        if (adminChip) adminChip.setAttribute('aria-expanded', 'false');
    }

    if (adminChip && adminDropdown) {
        adminChip.addEventListener('click', function (e) {
            e.stopPropagation();
            adminDropdown.hidden ? openAdminDropdown() : closeAdminDropdown();
        });

        adminChip.addEventListener('keydown', function (e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                adminDropdown.hidden ? openAdminDropdown() : closeAdminDropdown();
            }
            if (e.key === 'Escape') closeAdminDropdown();
        });

        document.addEventListener('click', function (e) {
            if (!adminDropdown.hidden &&
                !adminChip.contains(e.target) &&
                !adminDropdown.contains(e.target)) {
                closeAdminDropdown();
            }
        });

        document.addEventListener('keydown', function (e) {
            if (e.key === 'Escape') closeAdminDropdown();
        });
    }

})();
