// Comportamiento exclusivo de servicios-tarjetas.html.
const cardsHeader = document.querySelector('.site-header');

if (cardsHeader) {
  const updateCardsHeaderScroll = () => {
    cardsHeader.classList.toggle('is-scrolled', window.scrollY > 10);
  };

  window.addEventListener('scroll', updateCardsHeaderScroll, { passive: true });
  updateCardsHeaderScroll();
}

const filterButtons = document.querySelectorAll('.filter-btn');
const serviceCards = document.querySelectorAll('.service-card');
const noServicesMessage = document.getElementById('no-services-msg');

filterButtons.forEach((button) => {
  button.addEventListener('click', () => {
    const filter = button.dataset.filter;
    let visibleCards = 0;

    filterButtons.forEach((item) => item.classList.remove('is-active'));
    button.classList.add('is-active');

    serviceCards.forEach((card) => {
      const isVisible = filter === 'all' || card.dataset.category === filter;
      card.classList.toggle('is-hidden', !isVisible);
      if (isVisible) visibleCards += 1;
    });

    noServicesMessage?.classList.toggle('d-none', visibleCards !== 0);
  });
});
