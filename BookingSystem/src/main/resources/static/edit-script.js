// Add click listeners to all existing "delete row" buttons
document.addEventListener('DOMContentLoaded', () => {
    for (const button of document.querySelectorAll('.delete-button')) {
      button.addEventListener('pointerdown', (e) => deleteButton(e));
    }
  
    const scheduleTable = document.querySelector('#schedule-table');
    const emptyRowTemplate = document.querySelector('#empty-row-template');
  
    const addRowButton = document.querySelector('#add-row');
    addRowButton.addEventListener('pointerdown', (e) => {
      addNewEmptyRow();
    });
  
    const submitButton = document.querySelector('#submit-button');
    submitButton.addEventListener('pointerdown', (e) => submitForm());
  
    function addNewEmptyRow() {
      const newRow = emptyRowTemplate.content.cloneNode(true);
      newRow.querySelector('.delete-button').addEventListener('pointerdown', (e) => deleteButton(e));
      scheduleTable.children[0].appendChild(newRow);
    }
  
    function deleteButton(pointerEvent) {
      const row = pointerEvent.target.parentElement.parentElement;
      row.parentElement.removeChild(row);
    }
  
    function relabelRows() {
      const rows = scheduleTable.children[0].children;
      let rowNumber = 0;
      for (const row of rows) {
        const dateInput = row.querySelectorAll('input')[0];
        const timesInput = row.querySelectorAll('input')[1];
        dateInput.name = `schedule[${rowNumber}].date`;
        timesInput.name = `schedule[${rowNumber}].times`;
        rowNumber++;
      }
    }
  
    function submitForm() {
      relabelRows();
      document.querySelector('#main-form').submit();
    }
  });
  