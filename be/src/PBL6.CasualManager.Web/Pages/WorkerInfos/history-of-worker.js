$(function () {
    const l = abp.localization.getResource('CasualManager');

    const service = pBL6.casualManager.orders.order;

    const dataTable = $('#history-datatable').DataTable(abp.libs.datatables.normalizeConfiguration({
        processing: true,
        responsive: true,
        serverSide: true,
        paging: true,
        searching: false,
        autoWidth: true,
        fixedColumns: true,
        fixedHeader: true,
        bLengthChange: true,
        scrollCollapse: true,
        ordering: false,
        ajax: abp.libs.datatables.createAjax(service.getListByWorker, GetValueSearch),
        columnDefs: [
            {
                title: l('JobInfo:Name'),
                data: {},
                render: function (data) {
                    return `<img class="icon-type-of-job" src = "${data.iconTypeOfJob}"> ${data.jobName}`;
                },
                width: "15%"
            },
            {
                title: l('Customer:Name'),
                data: "customerName",
            },
            {
                title: l('Order:Date'),
                data: "orderDate",
                render: function (data) {
                    return luxon
                        .DateTime
                        .fromISO(data, {
                            locale: abp.localization.currentCulture.name
                        }).toFormat('dd/MM/yyyy');
                }
            },
            {
                title: l('JobInfo:Prices'),
                data: 'jobPrices',
                render: function (data) {
                    return `<p class="price">${new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(data) }</p>`
                },
                width: "10%"
            },
            {
                title: l('Order:FeeForWorker'),
                data: 'feeForWorker',
                render: function (data) {
                    return `<p class="price">${new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(data)}</p>`
                },
                width: "10%"
            },
            {
                title: l('JobInfoOfWorker:Note'),
                data: "note",
            },
            {
                title: l('Order:Status'),
                data: {},
                render: function (data) {
                    if (data.status == 0) {
                        return `<p class="order-status-is-in-process">${l(data.statusStr)}</p>`
                    }
                    if (data.status == 1) {
                        return `<p class="order-status-is-cancel">${l(data.statusStr)}</p>`
                    }
                    return `<p class="order-status-is-complete">${l(data.statusStr)}</p>`
                },
                width: "10%"
            },
            {
                title: l('Order:IsPaid'),
                data: "isPaid",
                render: function (data) {
                    return data ? `<span class="fa fa-check-circle is-checked"></span>` : `<span class="fa fa-times-circle isnt-checked"></span>`
                },
                width: "10%",
                class: "text-center"
            },
            {
                title: l('Delete'),
                rowAction: {
                    items: [{
                        text: '<i class="fa fa-trash-o danger"></i>',
                        visible: abp.auth.isGranted('CasualManager.Order.Delete'),
                        confirmMessage: function (data) {
                            return l('Common:DeletionConfirmationMessage');
                        },
                        action: function (data) {
                            service.delete(data.record.id)
                                .then(function () {
                                    abp.notify.info(l('Common:SuccessfullyDeleted'));
                                    dataTable.ajax.reload();
                                });
                        },
                        displayNameHtml: true,
                    }]
                },
                width: "5%",
                class: "text-center"
            },
        ],
    })
    );

    function GetValueSearch() {
        return {
            'workerId': $("#ViewModel_WorkerId").val()
        };
    }

    // Global Options
    Chart.defaults.global.defaultFontFamily = 'Lato';
    Chart.defaults.global.defaultFontSize = 18;
    Chart.defaults.global.defaultFontColor = '#777';

    service.getListOrderOfMonthsOfWorker($("#ViewModel_WorkerId").val(), 6)
        .then(function (result) {
            if (jQuery.isEmptyObject(result)) {
                $("#order-statisic-chart").addClass("hidden");
                $("#error-not-order").removeClass("hidden");
            }
            let labels = []
            let months = Object.keys(result).length
            let dataComplete = Array.from({ length: months }, (_, i) => 0)
            let dataCancel = Array.from({ length: months }, (_, i) => 0)
            let idx = 0
            for (const [key, value] of Object.entries(result)) {
                labels.push(`Tháng ${key}`)
                for (const [keyS, valueS] of Object.entries(value)) {
                    if (keyS === 'IsComplete') {
                        dataComplete[idx] = valueS
                    }
                    if (keyS === 'IsCancel') {
                        dataCancel[idx] = valueS
                    }
                }
                idx++;
            }
            let orderChart = document.getElementById('order-statisic-chart').getContext('2d');
            let chart = new Chart(orderChart, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: l('Worker:OrderInMonths:Complete'),
                        data: dataComplete,
                        backgroundColor: '#3B992F',
                        borderWidth: 1,
                        borderColor: '#777',
                        hoverBorderColor: '#000'
                    }, {
                        label: l('Worker:OrderInMonths:Cancel'),
                        data: dataCancel,
                        backgroundColor: '#E91E1E',
                        borderWidth: 1,
                        borderColor: '#777',
                        hoverBorderColor: '#000'
                    }]
                },
                options: {
                    title: {
                        display: true,
                        text: l('Worker:OrderInMonths', months),
                        fontSize: 25
                    },
                    tooltips: {
                        enabled: true
                    },
                    layout: {
                        padding: { left: 0, right: 0, bottom: 0, top: 0 }
                    },
                    scales: {
                        xAxes: [{
                            stacked: true
                        }],
                        yAxes: [{
                            stacked: true
                        }]
                    }
                }
            });
        })

    service.getListRevenueOfMonthsOfWorker($("#ViewModel_WorkerId").val(), 6)
        .then(function (result) {
            if (jQuery.isEmptyObject(result)) {
                $("#revenue-statisic-chart").addClass("hidden");
                $("#error-not-revenue").removeClass("hidden");
            }
            let labels = []
            let data = []
            let count = 0
            for (const [key, value] of Object.entries(result)) {
                labels.push(`Tháng ${key}`)
                data.push(value)
                count += 1
            }

            let revenueChart = document.getElementById('revenue-statisic-chart').getContext('2d');
            let massPopChart = new Chart(revenueChart, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: l('Common:Revenue'),
                        data: data,
                        backgroundColor: [
                            'rgba(54, 162, 235, 0.6)', 'rgba(255, 206, 86, 0.6)', 'rgba(75, 192, 192, 0.6)',
                            'rgba(153, 102, 255, 0.6)', 'rgba(255, 159, 64, 0.6)', 'rgba(255, 99, 132, 0.6)'
                        ],
                        borderWidth: 1,
                        borderColor: '#777',
                        hoverBorderColor: '#000'
                    }]
                },
                options: {
                    title: {
                        display: true,
                        text: l('Worker:RevenueInMonths', count),
                        fontSize: 25,
                        padding: 30
                    },
                    legend: {
                        display: false
                    },
                    layout: {
                        padding: { left: 0, right: 0, bottom: 0, top: 0 }
                    },

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
                        yAxes: [{
                            display: true,
                            ticks: {
                                suggestedMin: 0,
                                beginAtZero: true,
                                callback: function (value, index, values) {
                                    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
                                }
                            }
                        }]
                    }
                }
            });
        })

    
    
});