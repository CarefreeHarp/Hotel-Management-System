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

const header = document.querySelector('.site-header');
const hero = document.getElementById('hero');

const publicMenuToggle = document.querySelector('[data-public-menu-toggle]');
const publicMenu = document.querySelector('[data-public-menu]');
if (publicMenuToggle && publicMenu) {
  publicMenuToggle.addEventListener('click', () => {
    const isOpen = !publicMenu.classList.contains('tw-hidden');
    publicMenu.classList.toggle('tw-hidden', isOpen);
    publicMenuToggle.setAttribute('aria-expanded', String(!isOpen));
  });
}

// VIDEO DE THE HOTEL: se reproduce una vez al entrar en la sección, sin bloquear la navegación.
const scrollVideo = document.querySelector('.scroll-video');
const videoSection = document.getElementById('video-section');
scrollVideo.playbackRate = 2;
let hasHotelVideoStarted = false;

function startHotelVideo() {
  if (hasHotelVideoStarted) return;
  hasHotelVideoStarted = true;
  scrollVideo.currentTime = 0;
  scrollVideo.play().catch(() => {});
}

const hotelVideoObserver = new IntersectionObserver(([entry]) => {
  if (entry.isIntersecting) startHotelVideo();
}, { threshold: 0.2 });
hotelVideoObserver.observe(videoSection);

// CAMBIA EL ESTADO DEL HEADER APENAS SALE DEL HERO
const headerObserver = new IntersectionObserver(
  (entries) => {
    entries.forEach((entry) => {
      header.classList.toggle('is-scrolled', !entry.isIntersecting);
    });
  },
  { threshold: 0, rootMargin: '-72px 0px 0px 0px' }
);

headerObserver.observe(hero);

const updateHeaderScroll = () => header.classList.toggle('is-scrolled', scrollY > 10);
addEventListener('scroll', updateHeaderScroll, { passive: true });
updateHeaderScroll();

const updateVideoHeader = () => {
  const { top, bottom } = videoSection.getBoundingClientRect();
  const isVideoActive = top <= 0 && bottom >= innerHeight;
  header.classList.toggle('is-video-section', isVideoActive);
};
addEventListener('scroll', updateVideoHeader, { passive: true });
updateVideoHeader();

// BARRA DE PROGRESO DE SCROLL 
const scrollProgress = document.getElementById('scroll-progress');

function updateScrollProgress() {
  const scrolled = window.scrollY;
  const maxScroll = document.documentElement.scrollHeight - window.innerHeight;
  const percent = maxScroll > 0 ? (scrolled / maxScroll) * 100 : 0;
  scrollProgress.style.width = `${percent}%`;
  scrollProgress.setAttribute('aria-valuenow', Math.round(percent));
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
    button.closest('.room-card').classList.toggle(
      'is-flipped',
      button.dataset.cardAction === 'details'
    );
  });
});

// Los detalles de cada suite usan controles propios para no interferir con el carrusel principal.
document.querySelectorAll('.room-detail-carousel').forEach((detailCarousel) => {
  detailCarousel.classList.remove('carousel', 'slide');
  detailCarousel.removeAttribute('data-bs-interval');

  const slidesContainer = detailCarousel.querySelector('.carousel-inner');
  slidesContainer.classList.replace('carousel-inner', 'room-detail-slides');

  const slides = [...slidesContainer.children];
  slides.forEach((slide) => {
    slide.classList.remove('carousel-item', 'active');
    slide.classList.add('room-detail-slide');
  });
  slides[0].classList.add('is-active');

  detailCarousel.querySelectorAll('.room-detail-controls button').forEach((button) => {
    const direction = button.dataset.bsSlide;
    button.removeAttribute('data-bs-target');
    button.removeAttribute('data-bs-slide');

    button.addEventListener('click', () => {
      const currentIndex = slides.findIndex((slide) => slide.classList.contains('is-active'));
      const nextIndex = direction === 'next'
        ? (currentIndex + 1) % slides.length
        : (currentIndex - 1 + slides.length) % slides.length;

      slides[currentIndex].classList.remove('is-active');
      slides[nextIndex].classList.add('is-active');
    });
  });
});

// El carrusel muestra los tipos de habitación reales en una ventana deslizante.
const suiteCarousel = document.getElementById('room-grid');
const suiteCarouselInner = suiteCarousel.querySelector(':scope > .carousel-inner');
const roomTypeSources = [...suiteCarouselInner.querySelectorAll('.room-type-source')];

function createSuiteCard(source, index) {
  const card = document.createElement('article');
  card.className = `room-card room-card--${index % 2 === 0 ? 'ocean' : 'garden'}`;

  const image = document.createElement('img');
  image.src = source.dataset.image;
  image.alt = source.dataset.name;
  image.loading = 'lazy';

  const eyebrow = document.createElement('p');
  eyebrow.className = 'eyebrow';
  eyebrow.textContent = 'Room type';

  const imageWrapper = document.createElement('div');
  imageWrapper.className = 'room-card-swatch';
  imageWrapper.append(image);

  const name = document.createElement('h3');
  name.className = 'room-card-name';
  name.textContent = source.dataset.name;

  const details = document.createElement('p');
  details.className = 'room-card-sub';
  details.textContent = `${source.dataset.capacity} guests · COP ${Number(source.dataset.price).toLocaleString('en-US')} / night`;

  const description = document.createElement('p');
  description.className = 'room-type-description';
  description.textContent = source.dataset.description;

  card.append(eyebrow, imageWrapper, name, details, description);
  return card;
}

const suiteCards = roomTypeSources.map(createSuiteCard);
suiteCarouselInner.replaceChildren();

const suiteTrack = document.createElement('div');
suiteTrack.className = 'suite-carousel-track';
suiteCards.forEach((card) => suiteTrack.append(card));
suiteCarouselInner.append(suiteTrack);

const suiteIndicators = suiteCarousel.querySelector('.carousel-indicators');
suiteIndicators.replaceChildren();
suiteCards.forEach((_, index) => {
  const indicator = document.createElement('button');
  indicator.type = 'button';
  indicator.setAttribute('aria-label', `Room type ${index + 1}`);
  if (index === 0) {
    indicator.classList.add('active');
    indicator.setAttribute('aria-current', 'true');
  }
  indicator.addEventListener('click', () => moveSuiteCarousel(index));
  suiteIndicators.append(indicator);
});

let suiteIndex = 0;

function visibleSuiteCount() {
  if (window.matchMedia('(min-width: 1024px)').matches) return 3;
  if (window.matchMedia('(min-width: 640px)').matches) return 2;
  return 1;
}

function moveSuiteCarousel(nextIndex) {
  const maxIndex = Math.max(0, suiteCards.length - visibleSuiteCount());
  suiteIndex = Math.max(0, Math.min(nextIndex, maxIndex));
  const cardWidth = suiteCards[0]?.getBoundingClientRect().width || 0;
  const gap = Number.parseFloat(getComputedStyle(suiteTrack).gap) || 0;
  suiteTrack.style.transform = `translateX(-${suiteIndex * (cardWidth + gap)}px)`;

  [...suiteIndicators.children].forEach((indicator, index) => {
    const isActive = index === suiteIndex;
    indicator.classList.toggle('active', isActive);
    indicator.toggleAttribute('aria-current', isActive);
  });
}

suiteCarousel.querySelector('[data-bs-slide="prev"]').addEventListener('click', () => moveSuiteCarousel(suiteIndex - 1));
suiteCarousel.querySelector('[data-bs-slide="next"]').addEventListener('click', () => moveSuiteCarousel(suiteIndex + 1));
addEventListener('resize', () => moveSuiteCarousel(suiteIndex));
