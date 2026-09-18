document.addEventListener('DOMContentLoaded', () => {
  const monthsContainer = document.querySelector('[data-calendar-months]');
  if (!monthsContainer) return;
  const bookingForm = document.querySelector('[data-booking-form]');
  const rangeLabel = document.querySelector('[data-calendar-range]');
  const checkInInput = document.querySelector('[data-check-in-input]');
  const checkOutInput = document.querySelector('[data-check-out-input]');
  const checkInLabel = document.querySelector('[data-check-in-label]');
  const checkOutLabel = document.querySelector('[data-check-out-label]');
  const durationLabel = document.querySelector('[data-duration-label]');
  const formatter = new Intl.DateTimeFormat('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
  const weekdayNames = ['S', 'M', 'T', 'W', 'T', 'F', 'S'];
  const [year, month, day] = (bookingForm?.dataset.applicationToday ?? '').split('-').map(Number);
  const today = Number.isInteger(year) && Number.isInteger(month) && Number.isInteger(day)
    ? new Date(year, month - 1, day)
    : new Date();
  today.setHours(0, 0, 0, 0);
  let visibleMonth = new Date(today.getFullYear(), today.getMonth(), 1);
  let checkIn = new Date(today); checkIn.setDate(today.getDate() + 1);
  let checkOut = new Date(today); checkOut.setDate(today.getDate() + 3);
  let selectingCheckIn = true;
  const toValue = (date) => [date.getFullYear(), String(date.getMonth() + 1).padStart(2, '0'), String(date.getDate()).padStart(2, '0')].join('-');
  const sameDate = (left, right) => left.getTime() === right.getTime();
  const updateSummary = () => {
    checkInInput.value = toValue(checkIn); checkOutInput.value = toValue(checkOut);
    checkInLabel.textContent = formatter.format(checkIn); checkOutLabel.textContent = formatter.format(checkOut);
    durationLabel.textContent = `${Math.round((checkOut - checkIn) / 86400000)} nights`;
  };
  const chooseDate = (date) => {
    if (date < today) return;
    if (selectingCheckIn) {
      checkIn = date;
      if (checkOut <= checkIn) { checkOut = new Date(checkIn); checkOut.setDate(checkIn.getDate() + 1); }
      selectingCheckIn = false;
    } else if (date > checkIn) {
      checkOut = date;
      selectingCheckIn = true;
    } else {
      checkIn = date;
    }
    updateSummary(); renderCalendars();
  };
  const renderMonth = (month) => {
    const wrapper = document.createElement('section'); wrapper.className = 'calendar-month';
    const heading = document.createElement('h3'); heading.textContent = month.toLocaleDateString('en-US', { month: 'long', year: 'numeric' }); wrapper.append(heading);
    const weekdays = document.createElement('div'); weekdays.className = 'calendar-weekdays'; weekdayNames.forEach((name) => { const day = document.createElement('span'); day.textContent = name; weekdays.append(day); }); wrapper.append(weekdays);
    const days = document.createElement('div'); days.className = 'calendar-days';
    const firstDay = new Date(month.getFullYear(), month.getMonth(), 1).getDay(); const totalDays = new Date(month.getFullYear(), month.getMonth() + 1, 0).getDate();
    for (let empty = 0; empty < firstDay; empty += 1) days.append(document.createElement('span'));
    for (let number = 1; number <= totalDays; number += 1) { const date = new Date(month.getFullYear(), month.getMonth(), number); const button = document.createElement('button'); button.type = 'button'; button.className = 'calendar-day'; button.textContent = number; button.disabled = date < today; if (sameDate(date, checkIn) || sameDate(date, checkOut)) button.classList.add('is-selected'); if (date > checkIn && date < checkOut) button.classList.add('is-in-range'); button.addEventListener('click', () => chooseDate(date)); days.append(button); }
    wrapper.append(days); return wrapper;
  };
  const renderCalendars = () => { monthsContainer.replaceChildren(renderMonth(visibleMonth), renderMonth(new Date(visibleMonth.getFullYear(), visibleMonth.getMonth() + 1, 1))); rangeLabel.textContent = `${visibleMonth.toLocaleDateString('en-US', { month: 'long' })} — ${new Date(visibleMonth.getFullYear(), visibleMonth.getMonth() + 1, 1).toLocaleDateString('en-US', { month: 'long', year: 'numeric' })}`; };
  document.querySelector('[data-calendar-previous]').addEventListener('click', () => { visibleMonth = new Date(visibleMonth.getFullYear(), visibleMonth.getMonth() - 1, 1); renderCalendars(); });
  document.querySelector('[data-calendar-next]').addEventListener('click', () => { visibleMonth = new Date(visibleMonth.getFullYear(), visibleMonth.getMonth() + 1, 1); renderCalendars(); });
  const guestsOutput = document.querySelector('[data-guests-output]'); const guestsInput = document.querySelector('[data-guests-input]'); let guests = 1;
  const updateGuests = () => { guestsOutput.textContent = guests; guestsInput.value = guests; };
  document.querySelector('[data-guests-decrease]').addEventListener('click', () => { guests = Math.max(1, guests - 1); updateGuests(); });
  document.querySelector('[data-guests-increase]').addEventListener('click', () => { guests += 1; updateGuests(); });
  updateSummary(); renderCalendars(); updateGuests();
});
