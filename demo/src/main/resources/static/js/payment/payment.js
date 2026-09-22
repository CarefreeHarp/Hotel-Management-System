const cardNumber = document.querySelector('[data-card-number]');
cardNumber?.addEventListener('input', () => {
  const compactValue = cardNumber.value.replace(/\s/g, '');
  cardNumber.value = compactValue.match(/.{1,4}/g)?.join(' ') ?? '';
});

const expiryDate = document.querySelector('[data-expiry-date]');
expiryDate?.addEventListener('input', () => {
  const compactValue = expiryDate.value.replace(/\//g, '');
  expiryDate.value = compactValue.length > 2
    ? `${compactValue.slice(0, 2)}/${compactValue.slice(2)}`
    : compactValue;
});

const paymentAmount = document.querySelector('[data-payment-amount]');
const paymentSubmitAmount = document.querySelector('[data-payment-submit-amount]');
paymentAmount?.addEventListener('input', () => {
  const amount = Number(paymentAmount.value);
  if (paymentSubmitAmount && Number.isFinite(amount) && amount > 0) {
    paymentSubmitAmount.textContent = `$ ${amount.toLocaleString('es-CO')}`;
  }
});
