const filterButtons = document.querySelectorAll('.filter-btn');
const serviceCards = document.querySelectorAll('.service-card');
const noServicesMessage = document.getElementById('no-services-msg');
const pageButtons = document.querySelectorAll('[data-service-page]');
const previousPageButton = document.querySelector('[data-page-direction="previous"]');
const nextPageButton = document.querySelector('[data-page-direction="next"]');
const pages = ['one', 'two'];
let activePage = 'one';

function updateVisibleCards() {
  const activeFilter = document.querySelector('.filter-btn.is-active')?.dataset.filter ?? 'all';
  let visibleCards = 0;

  serviceCards.forEach((card) => {
    const matchesPage = card.dataset.page === activePage;
    const matchesFilter = activeFilter === 'all' || card.dataset.category === activeFilter;
    const isVisible = matchesPage && matchesFilter;
    card.classList.toggle('is-hidden', !isVisible);
    if (isVisible) visibleCards += 1;
  });

  noServicesMessage?.classList.toggle('d-none', visibleCards !== 0);
}

filterButtons.forEach((button) => {
  button.addEventListener('click', () => {
    filterButtons.forEach((item) => item.classList.remove('is-active'));
    button.classList.add('is-active');
    updateVisibleCards();
  });
});

function selectPage(page) {
  activePage = page;
  pageButtons.forEach((button) => {
    const isActive = button.dataset.servicePage === activePage;
    button.classList.toggle('is-active', isActive);
    if (isActive) {
      button.setAttribute('aria-current', 'page');
    } else {
      button.removeAttribute('aria-current');
    }
  });

  previousPageButton.disabled = activePage === pages[0];
  nextPageButton.disabled = activePage === pages.at(-1);
  updateVisibleCards();
}

pageButtons.forEach((button) => {
  button.addEventListener('click', () => selectPage(button.dataset.servicePage));
});

previousPageButton?.addEventListener('click', () => {
  const previousIndex = Math.max(0, pages.indexOf(activePage) - 1);
  selectPage(pages[previousIndex]);
});

nextPageButton?.addEventListener('click', () => {
  const nextIndex = Math.min(pages.length - 1, pages.indexOf(activePage) + 1);
  selectPage(pages[nextIndex]);
});

selectPage(activePage);
