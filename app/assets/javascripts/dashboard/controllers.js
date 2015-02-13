/**
 * Dashboard controllers.
 */
define([], function() {
  'use strict';

  /**
   * user is not a service, but stems from userResolve (Check ../user/services.js) object used by dashboard.routes.
   */
  var DashboardCtrl = function($scope, user, $http) {
    $scope.user = user;

    $scope.discoverTweets = true;
    $scope.discoverUsers = false;

    $scope.discoverTweetsSelected = function(){
        $scope.discoverTweets = true;
        $scope.discoverUsers = false;
    }
    $scope.discoverUsersSelected = function(){
      $scope.discoverTweets = false;
      $scope.discoverUsers = true;
    }

    $http({
      method: 'GET',
      url: 'http://localhost:9000/discover/tweets'
    }).success(function(data) {
      $scope.tweets = data;
    }).error(function(data) {
      console.error("Error on newfeed load: " + data);
    });
    $http({
      method: 'GET',
      url: 'http://localhost:9000/discover/users'
    }).success(function(data) {
      $scope.users = data;
    }).error(function(data) {
      console.error("Error on user suggestion load: " + data);
    });
  };
  DashboardCtrl.$inject = ['$scope', 'user', '$http'];

  var AdminDashboardCtrl = function($scope, user) {
    $scope.user = user;
  };
  AdminDashboardCtrl.$inject = ['$scope', 'user'];

  return {
    DashboardCtrl: DashboardCtrl,
    AdminDashboardCtrl: AdminDashboardCtrl
  };

});
