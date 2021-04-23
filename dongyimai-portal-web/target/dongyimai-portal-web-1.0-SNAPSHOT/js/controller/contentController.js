//控制层
app.controller('contentController', function ($scope, $controller, contentService) {

    $controller('baseController', {$scope: $scope});//继承


    //查询实体
    $scope.contentList=[]
    $scope.findContentById = function (categoryId) {
        contentService.findContentById(categoryId).success(
            function (response) {
                $scope.contentList[categoryId] = response;
            }
        );
    }


});	