const filterButtons = document.querySelectorAll('.filter-btn');
const serviceCards = document.querySelectorAll('.service-card--selectable');
const pageButtons = document.querySelectorAll('[data-service-page]');
const previousPageButton = document.querySelector('[data-page-direction="previous"]');
const nextPageButton = document.querySelector('[data-page-direction="next"]');
const cartItems = document.querySelector('[data-cart-items]');
const cartEmpty = document.querySelector('[data-cart-empty]');
const cartTotal = document.querySelector('[data-cart-total]');
const clearCartButton = document.querySelector('[data-clear-cart]');
const addServicesButton = document.querySelector('[data-add-services]');
const selectionForm = document.querySelector('[data-service-selection-form]');
const selectedServiceIds = new Set();
const pages = ['one', 'two'];
let activePage = 'one';

const formatCurrency = (amount) => `$ ${new Intl.NumberFormat('es-CO').format(amount)}`;

function updateVisibleCards() {
  const activeFilter = document.querySelector('.filter-btn.is-active')?.dataset.filter ?? 'all';
  let visibleCards = 0;
  serviceCards.forEach((card) => {
    const isVisible = card.dataset.page === activePage
      && (activeFilter === 'all' || card.dataset.category === activeFilter);
    card.classList.toggle('is-hidden', !isVisible);
    if (isVisible) visibleCards += 1;
  });
  document.getElementById('no-services-msg')?.classList.toggle('d-none', visibleCards !== 0);
}

function updateCart() {
  const selectedCards = [...serviceCards].filter((card) => selectedServiceIds.has(card.dataset.serviceId));
  cartItems.replaceChildren();
  let total = 0;
  selectedCards.forEach((card) => {
    const price = Number(card.dataset.servicePrice);
    total += price;
    const item = document.createElement('li');
    item.innerHTML = `<span>${card.dataset.serviceName}</span><strong>${formatCurrency(price)}</strong><button class="service-cart__remove" type="button" aria-label="Remove ${card.dataset.serviceName}" data-remove-service="${card.dataset.serviceId}">×</button>`;
    cartItems.append(item);
  });
  cartEmpty.hidden = selectedCards.length !== 0;
  cartTotal.textContent = `Total ${formatCurrency(total)}`;
  clearCartButton.disabled = selectedCards.length === 0;
  addServicesButton.disabled = selectedCards.length === 0;
  selectionForm.querySelectorAll('[data-selected-service-id]').forEach((input) => input.remove());
  selectedServiceIds.forEach((serviceId) => {
    const input = document.createElement('input');
    input.type = 'hidden';
    input.name = 'serviceIds';
    input.value = serviceId;
    input.dataset.selectedServiceId = '';
    selectionForm.append(input);
  });
}

function removeService(serviceId) {
  selectedServiceIds.delete(serviceId);
  const card = document.querySelector(`[data-service-id="${serviceId}"]`);
  card?.classList.remove('service-card--selected');
  const button = card?.querySelector('[data-select-service]');
  if (button) { button.disabled = false; button.textContent = 'Select service'; }
  updateCart();
}

serviceCards.forEach((card) => {
  card.querySelector('[data-select-service]').addEventListener('click', () => {
    const serviceId = card.dataset.serviceId;
    if (selectedServiceIds.has(serviceId)) return;
    selectedServiceIds.add(serviceId);
    card.classList.add('service-card--selected');
    const button = card.querySelector('[data-select-service]');
    button.disabled = true;
    button.textContent = 'Selected';
    updateCart();
  });
});

cartItems.addEventListener('click', (event) => {
  const button = event.target.closest('[data-remove-service]');
  if (button) removeService(button.dataset.removeService);
});
clearCartButton.addEventListener('click', () => [...selectedServiceIds].forEach(removeService));
filterButtons.forEach((button) => button.addEventListener('click', () => { filterButtons.forEach((item) => item.classList.remove('is-active')); button.classList.add('is-active'); updateVisibleCards(); }));
function selectPage(page) { activePage = page; pageButtons.forEach((button) => { const active = button.dataset.servicePage === page; button.classList.toggle('is-active', active); button.toggleAttribute('aria-current', active); }); previousPageButton.disabled = page === pages[0]; nextPageButton.disabled = page === pages.at(-1); updateVisibleCards(); }
pageButtons.forEach((button) => button.addEventListener('click', () => selectPage(button.dataset.servicePage)));
previousPageButton?.addEventListener('click', () => selectPage(pages[Math.max(0, pages.indexOf(activePage) - 1)]));
nextPageButton?.addEventListener('click', () => selectPage(pages[Math.min(pages.length - 1, pages.indexOf(activePage) + 1)]));
selectPage(activePage);
updateCart();
