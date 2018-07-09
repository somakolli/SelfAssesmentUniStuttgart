var ctx = document.getElementById('myChart').getContext('2d');
var chart = new Chart(ctx, {
    // The type of chart we want to create
    type: 'radar',

    // The data for our dataset
    data: {
    labels: ['Mathematik', 'Logik', 'Englisch', 'Programmieren'],
    datasets: [{
        data: [100, 85, 45, 0]
    }]
},
    // Configuration options go here
    options: {}
});