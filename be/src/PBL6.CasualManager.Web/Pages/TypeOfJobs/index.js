$(function () {
    const l = abp.localization.getResource('CasualManager');

    const service = pBL6.casualManager.typeOfJobs.typeOfJob;
    const createModal = new abp.ModalManager(abp.appPath + 'TypeOfJobs/CreateModal');
    const editModal = new abp.ModalManager(abp.appPath + 'TypeOfJobs/EditModal');

    const dataTable = $('#type-of-job-datatable').DataTable(abp.libs.datatables.normalizeConfiguration({
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
        ajax: abp.libs.datatables.createAjax(service.getListByName, GetValueSearch),
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
                title: l('TypeOfJob:Name'),
                data: "name",
                width: "20%"
            },
            {
                title: l('TypeOfJob:Description'),
                data: "description",
            },
            {
                title: l('Actions'),
                rowAction: {
                    items:
                        [
                            {
                                text: l('Edit'),
                                visible: abp.auth.isGranted('CasualManager.TypesOfJob.Update'),
                                action: function (data) {
                                    editModal.open({ id: data.record.id });
                                },
                                displayNameHtml: true,
                            },
                            {
                                text: l('Delete'),
                                visible: abp.auth.isGranted('CasualManager.TypesOfJob.Delete'),
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
                width: "20%",
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
        };
    }

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