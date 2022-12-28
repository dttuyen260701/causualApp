$(function () {
    const l = abp.localization.getResource('CasualManager');

    const service = pBL6.casualManager.plannings.planning;
    const createUpdateModal = new abp.ModalManager(abp.appPath + 'Plannings/CreateModal');

    const dataTable = $('#planning-datatable').DataTable(abp.libs.datatables.normalizeConfiguration({
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
        order: [[0, 'asc']],
        ajax: abp.libs.datatables.createAjax(service.getListRevenueTargetByYear, GetValueSearch),
        columnDefs: [
            {
                title: l('Planning:Month'),
                data: "month",
                render: function (data) {
                    return l("Common:Month", data)
                }
            },
            {
                title: l('Planning:Target'),
                data: "revenueTarget",
                render: function (data) {
                    return `<p class="price">${new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(data)}</p>`
                },
            },
            {
                title: l('Delete'),
                rowAction: {
                    items: [{
                        text: '<i class="fa fa-trash-o danger"></i>',
                        visible: abp.auth.isGranted('CasualManager.Planning.Delete'),
                        confirmMessage: function (data) {
                            return l('Common:DeletionConfirmationMessage', l("Common:Month", data.record.month));
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

    $('#search').click(function () {
        Search();
    });

    function Search() {
        dataTable.ajax.reload();
    }

    function GetValueSearch() {
        return {
            'filterYear': $("#ViewModel_FilterYear").val()
        };
    }

    $('#new-planning-job').click(function (e) {
        e.preventDefault();
        createUpdateModal.open();
    });

    createUpdateModal.onResult(function () {
        //dataTable.ajax.reload();
        abp.notify.success(l('Common:SuccessfullyCreated'));
        location.reload();
    });
});