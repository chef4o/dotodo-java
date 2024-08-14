const thisMonthInfo = document.getElementById('this-month-holidays');
const nextMonthInfo = document.getElementById('next-month-holidays');
const country_code = "BG"
const currentMonth = new Date().getMonth() + 1;
const currentYear = new Date().getFullYear();

fetch(`https://date.nager.at/api/v3/PublicHolidays/${currentYear}/${country_code}`)
    .then(response => response.json())
    .then(data => {
        console.log(data)
        const holidaysThisMonth = data
            .filter(holiday => {
                const holidayMonth = new Date(holiday.date).getMonth() + 1;
                return holidayMonth === currentMonth;
            })
            .map(holidayToString);
        const holidaysNextMonth = data
            .filter(holiday => {
                const holidayMonth = new Date(holiday.date).getMonth() + 1;
                return holidayMonth === (currentMonth + 1);
            })
            .map(holidayToString);
        console.log(holidaysThisMonth)
        holidaysThisMonth.length > 0
            ? thisMonthInfo.innerText = "Holidays this month: " + holidaysThisMonth.join(', ')
            : thisMonthInfo.innerText = "There are no holidays this month";

        console.log(holidaysNextMonth)
        holidaysNextMonth.length > 0
            ? nextMonthInfo.innerText = "Holidays next month: " + holidaysNextMonth.join(', ')
            : nextMonthInfo.innerText = "There are no holidays next month";
    });

function holidayToString(holiday) {
    const date = new Date(holiday.date);
    const day = date.getDate();
    const month = date.toLocaleString('en', {month: 'short'});
    return `${day} ${month} - ${holiday.name}`;
}