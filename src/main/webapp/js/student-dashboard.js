(function () {
    'use strict';

    /* ── Hamburger / sidebar toggle ── */
    const menuBtn        = document.getElementById('menuBtn');
    const sidebarOverlay = document.getElementById('studentSidebarOverlay');

    if (menuBtn) {
        menuBtn.addEventListener('click', function () {
            document.body.classList.toggle('sidebar-open');
        });
    }

    if (sidebarOverlay) {
        sidebarOverlay.addEventListener('click', function () {
            document.body.classList.remove('sidebar-open');
        });
    }

    /* ── Carousel ── */
    const carousel = document.getElementById('resourceCarousel');
    const prevBtn  = document.getElementById('carouselPrev');
    const nextBtn  = document.getElementById('carouselNext');

    if (carousel && prevBtn && nextBtn) {
        // scroll by one tile width + gap (220px tile + 14px gap)
        const scrollAmount = 234;
        prevBtn.addEventListener('click', function () {
            carousel.scrollBy({ left: -scrollAmount, behavior: 'smooth' });
        });
        nextBtn.addEventListener('click', function () {
            carousel.scrollBy({ left: scrollAmount, behavior: 'smooth' });
        });
    }

    /* ── Profile dropdown ── */
    const profileChip     = document.getElementById('profileChip');
    const profileDropdown = document.getElementById('profileDropdown');

    function openDropdown() {
        if (!profileDropdown) return;
        profileDropdown.hidden = false;
        profileChip.setAttribute('aria-expanded', 'true');
    }

    function closeDropdown() {
        if (!profileDropdown) return;
        profileDropdown.hidden = true;
        profileChip.setAttribute('aria-expanded', 'false');
    }

    if (profileChip && profileDropdown) {
        profileChip.addEventListener('click', function (e) {
            e.stopPropagation();
            profileDropdown.hidden ? openDropdown() : closeDropdown();
        });

        profileChip.addEventListener('keydown', function (e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                profileDropdown.hidden ? openDropdown() : closeDropdown();
            }
            if (e.key === 'Escape') closeDropdown();
        });

        document.addEventListener('click', function (e) {
            if (!profileDropdown.hidden &&
                !profileChip.contains(e.target) &&
                !profileDropdown.contains(e.target)) {
                closeDropdown();
            }
        });

        document.addEventListener('keydown', function (e) {
            if (e.key === 'Escape') closeDropdown();
        });
    }

})();
