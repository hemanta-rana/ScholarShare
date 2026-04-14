/* ── Navbar: scroll shadow ── */
const navbar  = document.getElementById('navbar');
const wrapper = document.getElementById('navbar-wrapper');

if (navbar && wrapper) {
  window.addEventListener('scroll', () => {
    if (window.scrollY > 50) navbar.classList.add('scrolled');
    else navbar.classList.remove('scrolled');
  });
}

/* ── Hamburger toggle ── */
const hamburger = document.getElementById('hamburger');
const mobileNav = document.getElementById('nav-mobile');

if (hamburger && mobileNav) {
  hamburger.addEventListener('click', () => {
    const open = hamburger.classList.toggle('open');
    navbar.classList.toggle('open', open);
    mobileNav.classList.toggle('open', open);
  });
}

function closeMenu() {
  if (hamburger) hamburger.classList.remove('open');
  if (navbar) navbar.classList.remove('open');
  if (mobileNav) mobileNav.classList.remove('open');
}

/* ── Scroll-reveal ── */
const revealEls = document.querySelectorAll('.reveal');

if (revealEls.length > 0) {
  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add('visible');
        observer.unobserve(entry.target);
      }
    });
  }, { threshold: 0.12 });

  revealEls.forEach(el => observer.observe(el));
}

/* ── Newsletter subscribe ── */
function handleSubscribe(e) {
  e.preventDefault();
  const input = e.target.previousElementSibling;
  const val = input.value.trim();
  if (!val || !val.includes('@')) {
    input.style.borderColor = '#e57373';
    setTimeout(() => input.style.borderColor = 'rgba(202,230,253,0.2)', 1500);
    return;
  }
  const btn = e.target;
  btn.textContent = '✓ Subscribed!';
  btn.style.background = '#5cb85c';
  btn.style.color = '#fff';
  input.value = '';
  setTimeout(() => {
    btn.textContent = 'Subscribe';
    btn.style.background = 'var(--sky)';
    btn.style.color = 'var(--navy)';
  }, 3000);
}

/* ── Active nav highlight on scroll ── */
const sections = document.querySelectorAll('section[id], footer[id]');
const navAnchors = document.querySelectorAll('.nav-links a');

if (sections.length > 0 && navAnchors.length > 0) {
  const sectionObserver = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        navAnchors.forEach(a => {
          a.style.borderBottomColor =
            a.getAttribute('href') === '#' + entry.target.id
              ? 'var(--sky)' : 'transparent';
        });
      }
    });
  }, { rootMargin: '-40% 0px -55% 0px' });

  sections.forEach(s => sectionObserver.observe(s));
}
