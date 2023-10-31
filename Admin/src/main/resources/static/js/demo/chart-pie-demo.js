var backgroundColor = [
    'rgba(0,255,13,0.7)', // Light Red
    'rgba(225,44,23,0.88)', // Light Green
    'rgba(255, 206, 86, 0.7)', // Light Yellow
];
var encodedDataElement = document.querySelector('[data-totalPrice-byPayment]');
var encodedData = encodedDataElement.getAttribute('data-totalPrice-byPayment');
var decodedData = decodeEntities(encodedData);

// Now you can parse the JSON data
var jsonData = JSON.parse(decodedData);

var labels = jsonData.map(function (item) {
    return item.payMethod;
});

var data = jsonData.map(function (item) {
    return item.amount;
});

var ctx = document.getElementById('myPieChart').getContext('2d');

var myPieChart = new Chart(ctx, {
    type: 'pie',
    data: {
        labels: labels,
        datasets: [{
            data: data,
            backgroundColor: backgroundColor,
        }]
    },
    options: {
        tooltips: {
            callbacks: {
                label: function (tooltipItem, data) {
                    var dataset = data.datasets[0];
                    var total = dataset.data.reduce(function (previousValue, currentValue, currentIndex, array) {
                        return previousValue + currentValue;
                    });
                    var currentValue = dataset.data[tooltipItem.index];
                    var percentage = Math.round((currentValue / total) * 100);
                    return percentage + "%";
                }
            }
        }
    }
});

