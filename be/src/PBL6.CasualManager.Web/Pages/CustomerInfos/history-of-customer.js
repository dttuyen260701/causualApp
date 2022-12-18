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
        ajax: abp.libs.datatables.createAjax(service.getListByCustomer, GetValueSearch),
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
                title: l('Order:WorkerName'),
                data: "workerName",
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
                title: l('Order:PricesCustomerPay'),
                data: 'jobPrices',
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
            'customerId': $("#ViewModel_CustomerId").val()
        };
    }
})