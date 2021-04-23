app.controller("brandController", function ($scope,$controller ,brandService) {
    //继承
    $controller('baseController',{$scope:$scope});


    $scope.findAll = function () {
        brandService.findAll().success(
            function (response) {
                $scope.list = response;

            }
        )
    };

     //分页
    $scope.findPage = function (page, rows) {
        brandService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;
            }
        )
    }

    $scope.save = function () {
        if ($scope.entity.id != null) {
            brandService.update($scope.entity).success(
                function (response) {
                    if (response.success) {
                        $scope.reloadList();
                        $scope.entity = {};
                    } else {
                        alert(response.message)
                    }
                })
        } else {

            brandService.add($scope.entity).success(
                function (response) {
                    if (response.success) {
                        $scope.reloadList();
                        $scope.entity = {};
                    } else {
                        alert(response.message)
                    }
                })
        }

    }
    $scope.findOne = function (id) {
        brandService.findOne(id).success(
            function (response) {
                $scope.entity = response;

            }
        )
    }

    $scope.delete = function () {
        brandService.delete($scope.selectId).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();
                }

            })
    }
    $scope.serchEntity = {};
    $scope.search = function (page, rows) {
        brandService.search(page, rows, $scope.serchEntity).success(
            function (response) {
                $scope.paginationConf.totalItems = response.total;
                $scope.list = response.rows;
            }
        )
    }
});

