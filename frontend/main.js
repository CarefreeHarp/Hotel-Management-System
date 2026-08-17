const navToggle = document.getElementById('nav-toggle');
const mainNav = document.getElementById('main-nav');

navToggle.addEventListener('click', () => {
  const isOpen = mainNav.classList.toggle('is-open');
  navToggle.setAttribute('aria-expanded', isOpen);
});

// close the mobile menu after picking a link
mainNav.querySelectorAll('.nav-link').forEach((link) => {
  link.addEventListener('click', () => {
    mainNav.classList.remove('is-open');
    navToggle.setAttribute('aria-expanded', 'false');
  });
});

// FADE IN DE LOS ELEMENTOS EN LA PAGINA
const fadeTargets = document.querySelectorAll('.fade-in');

const fadeObserver = new IntersectionObserver(
  (entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        entry.target.classList.add('is-visible');
        fadeObserver.unobserve(entry.target); // ESTO ES PARA QUE LA ANIMACION SOLO PASE UNA VEZ
      }
    });
  },
  { threshold: 0.15, rootMargin: '0px 0px -60px 0px' }
);

fadeTargets.forEach((target) => fadeObserver.observe(target));

// ESTA PARTE SI LA SAQUE DE GOOGLE, ES PARA QUE MUESTRE EL VIDEO SIN
// NECESIDAD DE CARGAR EL IFRAME DE YOUTUBE HASTA QUE LE DEN CLICK
const facade = document.querySelector('.youtube-facade');

function playPanoramicVideo() {
  const videoId = facade.dataset.youtubeId;
  const iframe = document.createElement('iframe');
  iframe.src = `https://www.youtube.com/embed/${videoId}?autoplay=1&rel=0`;
  iframe.title = 'Atlan Suites panoramic video';
  iframe.allow = 'autoplay; encrypted-media; picture-in-picture';
  iframe.allowFullscreen = true;
  facade.replaceWith(iframe);
}

facade.addEventListener('click', playPanoramicVideo);
facade.addEventListener('keydown', (event) => {
  if (event.key === 'Enter' || event.key === ' ') {
    event.preventDefault();
    playPanoramicVideo();
  }
});

// CAMBIA EL ESTADO DEL HEADER APENAS SALE DEL HERO
const header = document.querySelector('.site-header');
const hero = document.getElementById('hero');

const headerObserver = new IntersectionObserver(
  (entries) => {
    entries.forEach((entry) => {
      header.classList.toggle('is-scrolled', !entry.isIntersecting);
    });
  },
  { threshold: 0, rootMargin: '-72px 0px 0px 0px' }
);

headerObserver.observe(hero);

// BARRA DE PROGRESO DE SCROLL 
const scrollProgress = document.getElementById('scroll-progress');

function updateScrollProgress() {
  const scrolled = window.scrollY;
  const maxScroll = document.documentElement.scrollHeight - window.innerHeight;
  const percent = maxScroll > 0 ? (scrolled / maxScroll) * 100 : 0;
  scrollProgress.style.width = `${percent}%`;
}

window.addEventListener('scroll', updateScrollProgress);
updateScrollProgress();

// BOTONES QUE MUEVEN LA PAGINA A OTRA SECCION DE LA MISMA PAGINA
document.querySelectorAll('[data-scroll-target]').forEach((button) => {
  button.addEventListener('click', () => {
    const target = document.querySelector(button.dataset.scrollTarget);
    if (target) target.scrollIntoView({ behavior: 'smooth' });
  });
});

// CLICK PARA VOLTEAR LAS TARJETAS DE HABITACIONES 
document.querySelectorAll('.room-card-flip-btn').forEach((button) => {
  button.addEventListener('click', () => {
    button.closest('.room-card').classList.toggle('is-flipped');
  });
});