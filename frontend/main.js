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

// Al cambiar de suite, el carrusel vuelve a mostrar la cara principal.
const suiteCarousel = document.getElementById('room-grid');

// Agrupa tres tarjetas por página para el carrusel principal de Suites.
const suiteCarouselInner = suiteCarousel.querySelector(':scope > .carousel-inner');
const suiteCards = [...suiteCarouselInner.children].map((item) => item.querySelector('.room-card'));
suiteCarouselInner.replaceChildren();

const suitePages = [];
for (let index = 0; index < suiteCards.length; index += 3) {
  const page = document.createElement('div');
  page.className = `carousel-item${index === 0 ? ' active' : ''}`;

  const pageGrid = document.createElement('div');
  pageGrid.className = 'suite-carousel-page';
  suiteCards.slice(index, index + 3).forEach((card) => pageGrid.append(card));

  page.append(pageGrid);
  suiteCarouselInner.append(page);
  suitePages.push(page);
}

const suiteIndicators = suiteCarousel.querySelector('.carousel-indicators');
suiteIndicators.replaceChildren();
suitePages.forEach((_, index) => {
  const indicator = document.createElement('button');
  indicator.type = 'button';
  indicator.dataset.bsTarget = '#room-grid';
  indicator.dataset.bsSlideTo = index;
  indicator.setAttribute('aria-label', `Suite page ${index + 1}`);
  if (index === 0) {
    indicator.classList.add('active');
    indicator.setAttribute('aria-current', 'true');
  }
  suiteIndicators.append(indicator);
});

suiteCarousel.addEventListener('slide.bs.carousel', (event) => {
  if (event.target !== suiteCarousel) return;
  suiteCarousel.querySelectorAll('.room-card.is-flipped').forEach((card) => {
    card.classList.remove('is-flipped');
  });
});
