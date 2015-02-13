/**
 * Created by skobayashi on 15/02/13.
 */

var myApp = angular.module('myApp', []);
myApp.controller('MessageCtrl', ['$scope', function($scope) {
    $scope.message = 'set variable in Controller !';
}]);