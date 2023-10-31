var encodedDataElement = document.querySelector('[data-daily-earnings]');
if (encodedDataElement) {
    var encodedData = encodedDataElement.getAttribute('data-daily-earnings');
    var decodedData = decodeEntities(encodedData);

    // Now you can parse the JSON data
    var jsonData = JSON.parse(decodedData);

    // Extract dates and earnings from the data
    var dates = jsonData.map(function (item) {
        // Convert the "date" property to a Date object
        var date = new Date(item.date);
        // Format the date to display only the day
        var formattedDate = date.toLocaleDateString(undefined, {month: 'short', day: 'numeric'});
        return formattedDate;
    });

    var earnings = jsonData.map(function (item) {
        return item.earnings; // Extract the "earnings" property
    });

    // Create a chart using Chart.js
    var ctx = document.getElementById("myDailyEarningsChart").getContext('2d');

    var myChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: dates,
            datasets: [{
                label: "Daily Earnings in Rs.",
                data: earnings,
                borderColor: 'rgba(75, 192, 192, 1)',
                fill: true,
            }]
        },
        options: {
            scales: {
                x: {
                    beginAtZero: true
                },
                y: {
                    beginAtZero: true
                }
            }
        }
    });
} else {
    console.log('Element with data-daily-earnings attribute not found.');
}

function decodeEntities(encodedString) {
    var textArea = document.createElement('textarea');
    textArea.innerHTML = encodedString;
    return textArea.value;
}