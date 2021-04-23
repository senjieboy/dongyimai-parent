//用户表控制层
app.controller('userController', function ($scope, userService) {
    //User register

    $scope.register = function () {
        // Determine whether the two passwords entered by user are the same
        if ($scope.entity.password != $scope.password) {
            alert("The tow passwords are different, please enter again");
            return;
        }
        userService.add($scope.entity, $scope.code).success(function (response) {
            alert(response.messages);
        });
    }
    $scope.sendCode = function () {
        if ($scope.entity.phone == null) {
            alert("please enter phone number");
            return;
        }
        userService.sendCode($scope.entity.phone).success(function (response) {
            alert(response.message);
        })
    }
});	