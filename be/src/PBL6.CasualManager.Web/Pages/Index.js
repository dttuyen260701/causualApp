$(function () {
    const MAIN_COLOR = '#FEB74F'
    const SECOND_COLOR = '#6783C2'

    const CURRENT_DATE = new Date();

    const l = abp.localization.getResource('CasualManager');
    const statisicService = pBL6.casualManager.statisics.statisic;

    statisicService.getListRevenueOfMonthsOfBussiness(6)
        .then(function (result) {
            let startDay = CURRENT_DATE.getMonth() + 1 - 6 <= 0 ? `01/${12 + CURRENT_DATE.getMonth() + 1 - 6 + 1}/${CURRENT_DATE.getFullYear() - 1}` : `01/${CURRENT_DATE.getMonth() + 1 - 6 + 1}/${CURRENT_DATE.getFullYear() }`
            let endDate = `${CURRENT_DATE.getDate()}/${CURRENT_DATE.getMonth()+1}/${CURRENT_DATE.getFullYear()}`
            let labels = []
            let data_revenue = []
            let data_target = []
            for (const [key, value] of Object.entries(result)) {
                labels.push(`Tháng ${key}`)
                for (const [key2, value2] of Object.entries(value)) {
                    if (key2 == "revenue") {
                        data_revenue.push(value2)
                    } else {
                        data_target.push(value2)
                    }
                }
            }

            const data = {
                labels: labels,
                datasets: [{
                    label: l('Common:Target'),
                    backgroundColor: SECOND_COLOR,
                    borderColor: SECOND_COLOR,
                    data: data_target,
                    fill: false,
                }, {
                    label: l('Common:Revenue'),
                    backgroundColor: MAIN_COLOR,
                    borderColor: MAIN_COLOR,
                    data: data_revenue,
                    fill: false,
                }
                ],
            };

            const config = {
                type: 'bar',
                data: data,
                options: {
                    title: {
                        display: true,
                        text: l("Statisic:IncomeFromTo", startDay, endDate),
                        fontSize: 20,
                        padding: 1
                    },
                    layout: {
                        padding: { left: 0, right: 0, bottom: 0, top: 0 }
                    },
                    responsive: true,
                    interaction: {
                        mode: 'index',
                        intersect: false
                    },
                    maintainAspectRatio: false,
                    tooltips: {
                        enabled: true,
                        callbacks: {
                            label: function (tooltipItem, data) {
                                var label = data.datasets[tooltipItem.datasetIndex].label;
                                return `${label}: ${new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(tooltipItem.yLabel)}`;
                            }
                        }
                    },
                    scales: {
                        xAxes: [{
                            gridLines: {
                                display: false
                            }
                        }],
                        yAxes: [{
                            gridLines: {
                                display: false
                            },
                            ticks: {
                                suggestedMin: 0,
                                beginAtZero: true,
                                callback: function (value, index, values) {
                                    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
                                }
                            }
                        }]
                    }
                },
            };

            let myChart = document.getElementById('revenue-statisic-chart').getContext('2d');
            let chart = new Chart(myChart, config);
        })
});
