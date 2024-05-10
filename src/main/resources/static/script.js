document.addEventListener('DOMContentLoaded', function () {
    const searchInput = document.querySelector('.input-group input');
    const tableRows = document.querySelectorAll('tbody tr');
    const tableHeadings = document.querySelectorAll('thead th');
    const selectBtn = document.querySelector(".select-btn");

    // Searching
    function searchTable() {
        const searchValue = searchInput.value.toLowerCase();

        tableRows.forEach((row, i) => {
            let tableData = row.textContent.toLowerCase();
            let matchedIndexes = [];

            let index = tableData.indexOf(searchValue);
            while (index !== -1) {
                matchedIndexes.push(index);
                index = tableData.indexOf(searchValue, index + 1);
            }

            console.log('Matched indexes for row ' + i + ':', matchedIndexes);

            if (matchedIndexes.length > 0) {
                let newRowHTML = '';
                let lastIndex = 0;
                matchedIndexes.forEach(idx => {
                    newRowHTML += tableData.substring(lastIndex, idx);
                    newRowHTML += '<span class="highlight">' + tableData.substr(idx, searchValue.length) + '</span>';
                    lastIndex = idx + searchValue.length;
                });
                newRowHTML += tableData.substring(lastIndex);

                row.innerHTML = newRowHTML;
                row.classList.remove('hide');
            } else {
                row.classList.add('hide');
            }

            row.style.setProperty('--delay', i / 25 + 's');
        });

        document.querySelectorAll('tbody tr:not(.hide)').forEach((visibleRow, i) => {
            visibleRow.style.backgroundColor = (i % 2 == 0) ? 'transparent' : '#0000000b';
        });
    }

    searchInput.addEventListener('input', searchTable);

    // Sorting
    tableHeadings.forEach((head, i) => {
        let sortAsc = true;
        head.onclick = () => {
            tableHeadings.forEach(head => head.classList.remove('active'));
            head.classList.add('active');

            document.querySelectorAll('td').forEach(td => td.classList.remove('active'));
            tableRows.forEach(row => {
                row.querySelectorAll('td')[i].classList.add('active');
            });

            head.classList.toggle('asc', sortAsc);
            sortAsc = !head.classList.contains('asc');

            sortTable(i, sortAsc);
        };
    });

    function sortTable(column, sortAsc) {
        [...tableRows].sort((a, b) => {
            let firstRow = a.querySelectorAll('td')[column].textContent.toLowerCase();
            let secondRow = b.querySelectorAll('td')[column].textContent.toLowerCase();

            return sortAsc ? (firstRow < secondRow ? 1 : -1) : (firstRow < secondRow ? -1 : 1);
        }).map(sortedRow => document.querySelector('tbody').appendChild(sortedRow));
    }

    // Select Button and Items
    selectBtn.addEventListener("click", () => {
        selectBtn.classList.toggle("open");
    });

    const items = document.querySelectorAll(".item");
    items.forEach(item => {
        item.addEventListener("click", () => {
            item.classList.toggle("checked");

            let checked = document.querySelectorAll(".checked");
            let btnText = document.querySelector(".btn-text");

            if (checked && checked.length > 0) {
                btnText.innerText = `${checked.length} Selected`;
            } else {
                btnText.innerText = "Select Language";
            }
        });
    });
});
document.addEventListener('DOMContentLoaded', function () {
    const searchInput = document.getElementById('searchInput');
    const countrySuggestions = document.getElementById('countrySuggestions');

    // Define your list of countries
    const countries = ["Country 1", "Country 2", "Country 3"];

    // Update suggestions when the input changes
    searchInput.addEventListener('input', updateSuggestions);

    function updateSuggestions() {
        const inputValue = searchInput.value.toLowerCase();
        countrySuggestions.innerHTML = ''; // Clear previous suggestions

        // Filter countries based on input value and add them to suggestions
        const filteredCountries = countries.filter(country =>
            country.toLowerCase().startsWith(inputValue)
        );

        filteredCountries.forEach(country => {
            const option = document.createElement('option');
            option.value = country;
            countrySuggestions.appendChild(option);
        });
    }
});



