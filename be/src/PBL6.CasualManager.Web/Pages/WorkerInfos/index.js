$(function () {
    const l = abp.localization.getResource('CasualManager');

    const service = pBL6.casualManager.workerInfos.workerInfo;
    const createModal = new abp.ModalManager(abp.appPath + 'WorkerInfos/CreateModal');
    const editModal = new abp.ModalManager(abp.appPath + 'WorkerInfos/EditModal');

    const dataTable = $('#worker-datatable').DataTable(abp.libs.datatables.normalizeConfiguration({
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
        ajax: abp.libs.datatables.createAjax(service.getListWorkerAllInfo, GetValueSearch),
        columnDefs: [
            {
                title: l('Worker:Name'),
                data: "name",
            },
            {
                title: l('Common:Username'),
                data: "userName",
            },
            {
                title: l('Worker:IdentityCard'),
                data: "identityCard",
            },
            {
                title: l('Common:Phone'),
                data: "phone",
            },
            {
                title: l('Common:Email'),
                data: "email",
            },
            {
                title: l('Common:Gender'),
                data: function (data) {
                    return l(data.genderName)
                },
            },
            {
                title: l('Worker:WorkingTime'),
                data: function (data) {
                    return `${data.startWorkingTime} - ${data.endWorkingTime}`
                },
                class: "text-center"
            },
            {
                title: l('Worker:AverageRate'),
                data: "averageRate",
                render: function (data) {
                    let star = "";
                    for (let i = 1; i <= 5; i++) {
                        if (i <= data) {
                            star += `<span class="fa fa-star checked"></span>`
                        } else {
                            star += `<span class="fa fa-star"></span>`
                        }
                    }
                    return star
                },
                class: "text-center"
            },
            {
                title: l('Worker:Status'),
                data: "status",
                render: function (data) {
                    return data == 0 ? `<label class="free">${l('Worker:Status:Free')}</label>` : `<label class="busy">${l('Worker:Status:Busy')}</label>`
                },
                class: "text-center"
            },
            {
                title: l('Worker:IsActive'),
                data: "isActive",
                render: function (data) {
                    return data ? `<span class="fa fa-check-circle"></span>` : `<span class="fa fa-times-circle"></span>`
                },
                class: "text-center"
            },
            {
                title: l('Common:Address'),
                data: "addressDetail",
            },
            {
                title: l('Actions'),
                rowAction: {
                    items:
                        [
                            {
                                text: l('Edit'),
                                visible: abp.auth.isGranted('CasualManager.WorkerInfo.Update'),
                                action: function (data) {
                                    editModal.open({ id: data.record.id });
                                },
                                displayNameHtml: true,
                            },
                            {
                                text: l('Detail'),
                                visible: abp.auth.isGranted('CasualManager.WorkerInfo.Detail'),
                                action: function (data) {
                                    window.location.href = `/WorkerInfos/WorkerInfoDetail/${data.record.id}`;
                                    abp.ui.unblock();
                                },
                                displayNameHtml: true,
                            },
                            {
                                text: l('Delete'),
                                visible: abp.auth.isGranted('CasualManager.WorkerInfo.Delete'),
                                confirmMessage: function (data) {
                                    return l('Common:DeletionConfirmationMessage', data.record.name);
                                },
                                action: function (data) {
                                    service.deleteWorkerInfo(data.record.id)
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
            'keyword': $("#ViewModel_FilterName").val()
        };
    }

    $("#ViewModel_FilterTypeOfJob").on("change", function () {
        Search();
    });

    $('#new-worker').click(function (e) {
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