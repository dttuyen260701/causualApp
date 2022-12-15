$(function () {
    const l = abp.localization.getResource('CasualManager');

    const service = pBL6.casualManager.jobInfos.jobInfo;
    const createModal = new abp.ModalManager(abp.appPath + 'JobInfos/CreateModal');
    const editModal = new abp.ModalManager(abp.appPath + 'JobInfos/EditModal');

    const dataTable = $('#job-info-datatable').DataTable(abp.libs.datatables.normalizeConfiguration({
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
        ajax: abp.libs.datatables.createAjax(service.getListSearch, GetValueSearch),
        columnDefs: [
            {
                title: l('STT'),
                render: function (data, type, full, meta) {
                    const info = dataTable.page.info();
                    return info.page * dataTable.page.len() + meta.row + 1;
                },
                className: "text-center",
                width: "5%"
            },
            {
                title: l('JobInfo:Name'),
                data: "name",
                width: "20%"
            },
            {
                title: l('JobInfo:Description'),
                data: "description",
                width: "40%"
            },
            {
                title: l('JobInfo:Prices'),
                data: 'prices',
                render: function (data) {
                    return `<p class="price">${new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(data)}</p>`
                },
                width: "10%"
            },
            {
                title: l('JobInfo:TypeOfJobName'),
                data: {},
                render: function(data){
                    return `<img class="icon-type-of-job" src = "${data.typeOfJobIcon}"> ${data.typeOfJobName}`;
                },
                width: "10%"
            },
            {
                title: l('Actions'),
                rowAction: {
                    items:
                        [
                            {
                                text: l('Edit'),
                                visible: abp.auth.isGranted('CasualManager.JobInfo.Update'),
                                action: function (data) {
                                    editModal.open({ id: data.record.id });
                                },
                                displayNameHtml: true,
                            },
                            {
                                text: l('Delete'),
                                visible: abp.auth.isGranted('CasualManager.JobInfo.Delete'),
                                confirmMessage: function (data) {
                                    return l('Common:DeletionConfirmationMessage', data.record.name);
                                },
                                action: function (data) {
                                    service.delete(data.record.id)
                                        .then(function () {
                                            abp.notify.info(l('Common:SuccessfullyDeleted'));
                                            dataTable.ajax.reload();
                                        });
                                },
                                displayNameHtml: true,
                            }
                        ]
                },
                width: "15%",
                class: "text-center"
            }
        ],
    })
    );

    $('#search').click(function () {
        Search();
    });

    $('#ViewModel_FilterName').keypress(function (event) {
        let keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode == '13') {
            Search();
        }
    });

    function Search() {
        dataTable.ajax.reload();
    }

    function GetValueSearch() {
        return {
            'filterName': $("#ViewModel_FilterName").val(),
            'filterTypeOfJob': $("#ViewModel_FilterTypeOfJob").val(),
        };
    }

    $("#ViewModel_FilterTypeOfJob").on("change", function () {
        Search();
    });

    $('#new-type-of-job').click(function (e) {
        e.preventDefault();
        createModal.open();
    });

    createModal.onResult(function () {
        dataTable.ajax.reload();
        abp.notify.success(l('Common:SuccessfullyCreated'));
    });

    editModal.onResult(function () {
        dataTable.ajax.reload();
        abp.notify.success(l('Common:SuccessfullyEdited'));
    });
});