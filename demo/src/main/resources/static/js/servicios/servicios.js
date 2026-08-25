// TABLA DE SERVICIOS: hace clicable toda la fila, no solo el nombre del servicio.
// El enlace real vive en el nombre, así que la página sigue funcionando aunque el JS no cargue.
document.querySelectorAll('.services-row').forEach((fila) => {
  const enlaceDetalle = fila.querySelector('.service-link');

  fila.addEventListener('click', (evento) => {
    // Si el clic ya fue sobre el enlace, deja que el navegador navegue por su cuenta.
    if (evento.target.closest('a')) return;
    enlaceDetalle.click();
  });
});
