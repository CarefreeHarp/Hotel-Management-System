/**
 * LÓGICA INTERACTIVA PARA LA PANTALLA DE SERVICIOS
 * - Filtrado dinámico por categoría de servicios (Tarjetas y Tabla)
 * - Conmutador entre vista de Tarjetas (Cards) y vista de Lista (List)
 * - Animaciones suaves
 */
document.addEventListener('DOMContentLoaded', () => {
  const filterBtns = document.querySelectorAll('.filter-btn');
  const serviceCards = document.querySelectorAll('.service-card');
  const tableRows = document.querySelectorAll('.service-table-row');
  const noServicesMsg = document.getElementById('no-services-msg');

  const viewCardsBtn = document.getElementById('view-cards-btn');
  const viewListBtn = document.getElementById('view-list-btn');
  const servicesGridSection = document.getElementById('services-grid-wrapper');
  const servicesTableSection = document.getElementById('services-table-wrapper');

  // Alternar vista entre Tarjetas y Tabla
  if (viewCardsBtn && viewListBtn && servicesGridSection && servicesTableSection) {
    viewCardsBtn.addEventListener('click', () => {
      viewCardsBtn.classList.add('is-active');
      viewListBtn.classList.remove('is-active');
      servicesGridSection.classList.remove('d-none');
      servicesTableSection.classList.add('d-none');
    });

    viewListBtn.addEventListener('click', () => {
      viewListBtn.classList.add('is-active');
      viewCardsBtn.classList.remove('is-active');
      servicesTableSection.classList.remove('d-none');
      servicesGridSection.classList.add('d-none');
    });
  }

  // Filtrado por categoría
  if (!filterBtns.length) return;

  filterBtns.forEach((btn) => {
    btn.addEventListener('click', () => {
      const filter = btn.getAttribute('data-filter');

      // Actualizar estado activo en los botones de filtro
      filterBtns.forEach((b) => b.classList.remove('is-active'));
      btn.classList.add('is-active');

      let visibleCards = 0;

      // Filtrar Tarjetas
      serviceCards.forEach((card) => {
        const category = card.getAttribute('data-category');
        if (filter === 'all' || category === filter) {
          card.classList.remove('is-hidden');
          visibleCards++;
        } else {
          card.classList.add('is-hidden');
        }
      });

      // Filtrar Tabla
      tableRows.forEach((row) => {
        const category = row.getAttribute('data-category');
        if (filter === 'all' || category === filter) {
          row.classList.remove('d-none');
        } else {
          row.classList.add('d-none');
        }
      });

      // Mensaje cuando no hay resultados
      if (noServicesMsg) {
        if (visibleCards === 0) {
          noServicesMsg.classList.remove('d-none');
        } else {
          noServicesMsg.classList.add('d-none');
        }
      }
    });
  });
});

