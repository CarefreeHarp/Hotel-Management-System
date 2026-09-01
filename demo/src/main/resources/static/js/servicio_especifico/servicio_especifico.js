// Match the header scroll behavior used by index.html.
const serviceHeader = document.querySelector('.site-header');

const publicMenuToggle = document.querySelector('[data-public-menu-toggle]');
const publicMenu = document.querySelector('[data-public-menu]');
if (publicMenuToggle && publicMenu) {
  publicMenuToggle.addEventListener('click', () => {
    const isOpen = !publicMenu.classList.contains('tw-hidden');
    publicMenu.classList.toggle('tw-hidden', isOpen);
    publicMenuToggle.setAttribute('aria-expanded', String(!isOpen));
  });
}

const updateServiceHeaderScroll = () => {
  serviceHeader.classList.toggle('is-scrolled', window.scrollY > 10);
};

window.addEventListener('scroll', updateServiceHeaderScroll, { passive: true });
updateServiceHeaderScroll();

const galleryImages = document.querySelectorAll('.gallery-card img');

if (galleryImages.length) {
  const lightbox = document.createElement('div');
  lightbox.className = 'gallery-lightbox';
  lightbox.setAttribute('role', 'dialog');
  lightbox.setAttribute('aria-modal', 'true');
  lightbox.setAttribute('aria-label', 'Expanded service gallery image');
  lightbox.innerHTML = `
    <button class="lightbox-close" type="button" aria-label="Close expanded image">×</button>
    <img class="lightbox-image" alt="">
  `;
  document.body.append(lightbox);

  const lightboxImage = lightbox.querySelector('.lightbox-image');
  const closeButton = lightbox.querySelector('.lightbox-close');
  let lastFocusedElement;

  const closeLightbox = () => {
    lightbox.classList.remove('is-open');
    document.body.classList.remove('lightbox-open');
    lastFocusedElement?.focus();
  };

  galleryImages.forEach((image) => {
    image.closest('.gallery-card').tabIndex = 0;
    image.closest('.gallery-card').setAttribute('role', 'button');
    image.closest('.gallery-card').setAttribute('aria-label', `Expand ${image.alt}`);

    const openLightbox = () => {
      lastFocusedElement = document.activeElement;
      lightboxImage.src = image.currentSrc || image.src;
      lightboxImage.alt = image.alt;
      lightbox.classList.add('is-open');
      document.body.classList.add('lightbox-open');
      closeButton.focus();
    };

    image.closest('.gallery-card').addEventListener('click', openLightbox);
    image.closest('.gallery-card').addEventListener('keydown', (event) => {
      if (event.key === 'Enter' || event.key === ' ') {
        event.preventDefault();
        openLightbox();
      }
    });
  });

  closeButton.addEventListener('click', closeLightbox);
  lightbox.addEventListener('click', (event) => {
    if (event.target === lightbox) closeLightbox();
  });
  window.addEventListener('keydown', (event) => {
    if (event.key === 'Escape' && lightbox.classList.contains('is-open')) closeLightbox();
  });
}
