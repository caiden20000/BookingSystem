const changeStatusUrl = '/change-status/';

for (const button of document.querySelectorAll('[data-status]')) {
  button.addEventListener('click', (e) => {
    const requestUrl = changeStatusUrl + e.target.dataset.id + "?status=" + e.target.dataset.status;
    const result = window.confirm('Are you sure?');
    if (result) window.location.assign(window.location.origin + requestUrl);
  });
}
