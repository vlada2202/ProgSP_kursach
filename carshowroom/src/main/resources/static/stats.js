google.charts.load("current", {packages: ["corechart"]});
google.charts.setOnLoadCallback(drawBrand);
google.charts.setOnLoadCallback(drawDealer);

function drawBrand() {
    let res = [['Название', 'Прибыль', {role: 'style'}]];

    let colors = ['#ffd6ff', '#e7c6ff', '#c8b6ff', '#b8c0ff', '#bbd0ff'];

    let brandData = [];
    for (let i = 0; i < topName.length; i++) {
        brandData.push([topName[i], topNum[i], colors[i]]);
    }

    brandData.sort((a, b) => b[1] - a[1]);

    brandData = brandData.slice(0, 5);

    for (let i = 0; i < brandData.length; i++) {
        res.push(brandData[i]);
    }

    var data = google.visualization.arrayToDataTable(res);


    let options = {
        title: 'Топ 5 по прибыли',
        hAxis: {title: 'Марка'},
        vAxis: {title: 'Прибыль'},
        bar: {groupWidth: "80%"},
        legend: {position: "none"}
    };

    let chart = new google.visualization.ColumnChart(document.getElementById('drawBrand'));
    chart.draw(data, options);
}

function drawDealer() {
    let res = [['Дилер', 'Прибыль']];


    let numDealers = dealerString.length;


    let myColors = ['#ffb3c1', '#f4978e', '#d0f4de', '#e2cfea', '#e4c1f9'];

    for (let i = 0; i < dealerString.length; i++) {
        res.push([dealerString[i], dealerInt[i]]);
    }


    var data = google.visualization.arrayToDataTable(res);


    var options = {
        title: 'Прибыль дилеров',
        pieHole: 0.2,
        colors: myColors.slice(0, numDealers)
    };

    var chart = new google.visualization.PieChart(document.getElementById('drawDealer'));
    chart.draw(data, options);
}