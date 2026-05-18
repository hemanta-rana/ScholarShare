(function () {
    const menuBtn = document.getElementById('menuBtn');
    const sidebar = document.querySelector('.student-sidebar');
    const carousel = document.getElementById('resourceCarousel');
    const prevBtn = document.getElementById('carouselPrev');
    const nextBtn = document.getElementById('carouselNext');

    if (menuBtn) {
        menuBtn.addEventListener('click', function () {
            document.body.classList.toggle('sidebar-open');
        });
    }

    if (carousel && prevBtn && nextBtn) {
        const scrollAmount = 240;

        prevBtn.addEventListener('click', function () {
            carousel.scrollBy({ left: -scrollAmount, behavior: 'smooth' });
        });

        nextBtn.addEventListener('click', function () {
            carousel.scrollBy({ left: scrollAmount, behavior: 'smooth' });
        });
    }
})();
