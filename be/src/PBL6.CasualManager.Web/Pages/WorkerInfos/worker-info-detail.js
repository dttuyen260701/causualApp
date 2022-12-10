$(function () {
    const l = abp.localization.getResource('CasualManager');

    const service = pBL6.casualManager.jobInfoOfWorkers.jobInfoOfWorker;
    const createModal = new abp.ModalManager(abp.appPath + 'WorkerInfos/JobInfoOfWorkerCreateModal');
    const editModal = new abp.ModalManager(abp.appPath + 'WorkerInfos/JobInfoOfWorkerUpdateModal');

    const dataTable = $('#jobinfo-of-worker-datatable').DataTable(abp.libs.datatables.normalizeConfiguration({
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
                title: l('JobInfo:Name'),
                data: "jobName",
            },
            {
                title: l('TypeOfJob:Name'),
                data: "typeOfJobName",
            },
            {
                title: l('JobInfo:Prices'),
                data: function (data) {
                    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(data.prices);
                },
                width: "10%"
            },
            {
                title: l('JobInfoOfWorker:Note'),
                data: "note",
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
                                    editModal.open({ jobInfoOfWorkerId: data.record.id });
                                },
                                displayNameHtml: true,
                            },
                            {
                                text: l('Delete'),
                                visible: abp.auth.isGranted('CasualManager.JobInfoOfWorker.Delete'),
                                confirmMessage: function (data) {
                                    return l('JobInfoOfWorker:DeletionConfirmationMessage', data.record.jobName, $("#ViewModel_Name").val());
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

    function Search() {
        dataTable.ajax.reload();
    }

    function GetValueSearch() {
        return {
            'filterWorker': $("#ViewModel_WorkerId").val()
        };
    }

    $("#ViewModel_FilterTypeOfJob").on("change", function () {
        Search();
    });

    $('#new-job-info-of-worker').click(function (e) {
        e.preventDefault();
        createModal.open({ workerId: $("#ViewModel_WorkerId").val() });
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