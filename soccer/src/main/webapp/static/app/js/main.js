var worldCupApp = angular.module('worldCupApp', ['ngRoute']);

worldCupApp.service('pagingSvc', function($location){
	
	this.back = function(search_url, page){
		page = page - 1;
		return $location.path(search_url + page);
	};
	
	this.forward = function(search_url, page){
		page = page + 1;
		return $location.path(search_url + page);
	};
	
	this.first = function(search_url){
		return $location.path(search_url + 0);
	};
	
	this.last = function(search_url, pages){
		lastPage = pages - 1;
		return $location.path(search_url + lastPage);
	};
});

worldCupApp.service('editPageSvc', function() {
	
	storedPage = {};
	
	this.storePage = function(page){
		storedPage = page;
	}
	
	this.retrievePage = function(){
		return storedPage;
	}
	
});

worldCupApp.controller('worldCupCtrl', function($scope, $http, $location, $routeParams, pagingSvc, editPageSvc){
	
	$scope.base_url = "/api/worldcups";
	$scope.worldCups = [];
	$scope.worldCup = {};
	$scope.worldCup.winner = {};
	$scope.worldCup.host = {};
	
	$scope.page = Number($routeParams.page);
	$scope.num_pages = 0;
	
	var getWorldCups = function () {
		
		$http.get($scope.base_url, { params: {'page': $scope.page}})
			.then(function success(data) {
				$scope.worldCups = data.data;
				angular.forEach($scope.worldCups, function(worldCup){
					$scope.getHostCountry(worldCup);
					$scope.getWinnerCountry(worldCup);
				});
				$scope.num_pages = data.headers("pages");
				if ($scope.num_pages === null) {
					$location.path("/worldcups/page/" + Number($scope.page - 1));
				}
			}, function error(data) {
				console.log(status);
			})
		
	};
	
	getWorldCups();
	
	$scope.getHostCountry = function(worldCup){
		$http.get("/api/worldcups/" + worldCup.id + "/host")
		.then(function success(data){
			return worldCup.host = data.data;
		}, function error(data){
			console.log(data);
		})
	};
	
	$scope.getWinnerCountry = function(worldCup){
		$http.get("/api/worldcups/" + worldCup.id + "/winner")
		.then(function success(data){
			return worldCup.winner = data.data;
		}, function error(data){
			console.log(data);
		});
	};
	
	$scope.search_url = '/worldcups/page/';
	
	$scope.back = function(){
		pagingSvc.back($scope.search_url, $scope.page);
	};
	
	$scope.forward = function(){
		pagingSvc.forward($scope.search_url, $scope.page);
	};
	
	$scope.first = function(){
		pagingSvc.first($scope.search_url);
	};
	
	$scope.last = function(){
		pagingSvc.last($scope.search_url, $scope.num_pages);
	}
	
	$scope.add = function() {
		$location.path("/worldcups/add");
	};
	
	$scope.edit = function(id, page) {
		editPageSvc.storePage(page);
		$location.path("/worldcups/edit/" + id);
	};
	
	$scope.delete = function(id) {
		$http.delete("/api/worldcups/" + id)
			.then(function success(data){
				getWorldCups();
			}, function error(data){
				console.log(data);
			})
	};
	
	$scope.find = function() {
		$location.path("/worldcups/find");
	};
	
	$scope.view = function(id) {
		$location.path("/worldcups/view/" + id);
	};
	
});

worldCupApp.controller('addWorldCupCtrl', function ($scope, $http, $location){
	
	$scope.base_url = "/api/worldcups";

	$scope.countries_base_url = "/api/countries";
	$scope.countries = [];
	
	$scope.worldCup = {};
	$scope.worldCup.idWinner = "";
	$scope.worldCup.idHost = "";
	
	$scope.selectedWinner = {};
	$scope.selectedHost = {};
	
	$scope.num_pages = 0;

	var getCountries = function(){
		
		for (i = 1; i < $scope.num_pages; i++){
			$scope.page = i;
			var config = {params: {}};
			config.params.page = $scope.page;
			
			$http.get("/api/countries", config)
			.then(function success(data){
				$scope.countries = $scope.countries.concat(data.data);
				console.log($scope.countries);
			}, function error(data){
				console.log(data);
			});
		};
	};

	var getPages = function(){
		$http.get($scope.countries_base_url + "?page=0")
		.then(function success(data) {
			$scope.num_pages = data.headers("pages");
			$scope.countries = data.data;
			$scope.selectedHost = $scope.countries[0];
			$scope.selectedWinner = $scope.countries[0];
			getCountries();
		}, function error(status) {
			console.log(status);
		})
	};
	
	getPages();
	
	$scope.add = function () {
		
		$scope.worldCup.idWinner = $scope.selectedWinner.id;
		$scope.worldCup.idHost = $scope.selectedHost.id;
		
		$http.post($scope.base_url, $scope.worldCup)
		.then (function success (data) {
			$scope.worldCupsTotalPages = data.headers("pages");
			$location.path('/worldcups/page/' + ($scope.worldCupsTotalPages - 1));
		}, function error(status) {
			console.log(status);
		})
	};
});

worldCupApp.controller('viewWorldCupCtrl', function($scope, $http, $routeParams){
	
	$scope.worldCup = {};
	$scope.worldCup.host = "";
	$scope.worldCup.winner = ""
	$scope.rest_url = "/api/worldcups/";
	
	var getWorldCup = function(id){
		$http.get($scope.rest_url + $routeParams.id)
		.then(function success(data){
			$scope.worldCup = data.data;
			getHostCountry($scope.worldCup); 
			getWinnerCountry($scope.worldCup);
		}, function error(data){
			console.log(data);
		})
	};
	
	var getHostCountry = function(worldCup){
		$http.get("/api/worldcups/" + worldCup.id + "/host")
		.then(function success(data){
			worldCup.host = data.data;
		}, function error(data){
			console.log(data);
		})
	};
	
	var getWinnerCountry = function(worldCup){
		$http.get("/api/worldcups/" + worldCup.id + "/winner")
		.then(function success(data){
			worldCup.winner = data.data;
		}, function error(data){
			console.log(data);
		});
	};
	
	getWorldCup();
	
});

worldCupApp.controller('editWorldCupCtrl', function($scope, $http, $location, $routeParams, editPageSvc){
	
	$scope.base_url = "/api/worldcups/";
	$scope.page = editPageSvc.retrievePage();
	
	$scope.worldCup = {};
	$scope.worldCup.idHost = "";
	$scope.worldCup.idWinner = "";
	
	$scope.selectedWinner = {};
	$scope.selectedHost = {};
	
	var getWorldCup = function(id){
		$http.get($scope.base_url + $routeParams.id)
		.then(function success(data){
			$scope.worldCup = data.data;
			getWinnerCountry($scope.worldCup);
			getHostCountry($scope.worldCup); 
		}, function error(data){
			console.log(data);
		})
	};
	
	getWorldCup();
	
	getHostCountry = function(id){
		$http.get("/api/worldcups/" + $routeParams.id + "/host")
		.then(function success(data){
			$scope.worldCup.idHost = data.data.id;
			$scope.selectedHost = data.data.name;
		}, function error(data){
			console.log(data);
		})
	};
	
	var getWinnerCountry = function(id){
		$http.get("/api/worldcups/" + $routeParams.id + "/winner")
		.then(function success(data){
			$scope.worldCup.idWinner = data.data.id;
			$scope.selectedWinner = data.data.name;
		}, function error(data){
			console.log(data);
		});
	};
	
	$scope.edit = function () {
		$http.put( $scope.base_url + $routeParams.id, $scope.worldCup)
			.then ( function success (data, status) {
				console.log($scope.page);
				$location.path('/worldcups/page/' + $scope.page);
			}, function error (data, status) {
				console.log(data);
			});
	};
	
});

worldCupApp.controller('findWorldCupCtrl', function($scope, $http, $location, pagingSvc){
	
	$scope.num_pages = 0;
	$scope.page = 0;
	$scope.championshipSearch = {};
	$scope.championships = [];
	$scope.indicator = true;
	$scope.search_url = "";
	
	$scope.find = function(){
		getWC();
	};
	
	
	var getWC = function(){ 
		
		var config = {params: {}};
		
		config.params.page = $scope.page;
		
		if ($scope.championshipSearch.name != "") {
			config.params.name = $scope.championshipSearch.name;
		};
		
		if ($scope.championshipSearch.minY != "") {
			config.params.minY = $scope.championshipSearch.minY;
		};
		
		if ($scope.championshipSearch.maxY != "") {
			config.params.maxY = $scope.championshipSearch.maxY;
		};
		
		$scope.search_url = ("/api/worldcups/" + config.params.page + "&" + config.params.minY + "&" + config.params.maxY);
		console.log("search url: " + $scope.search_url);
		
		$scope.back = function(){
			console.log($scope.search_url);
			pagingSvc.back($scope.search_url, $scope.page);
		};
		
		$scope.forward = function(){
			pagingSvc.forward($scope.search_url, $scope.page);
		};
		
		$http.get("/api/worldcups", config)
			.then(function success(data){
				if (data.data != ""){
					$scope.indicator = false;
					console.log(data);
					console.log(data.headers("pages"));
				} else {
					$scope.indicator = true;
				}
				$scope.championships = data.data;
				$scope.num_pages = data.headers("pages");
			}, function error(data){
				console.log(data);
			});
		
		};

		
	
});

worldCupApp.controller('countriesCtrl', function($scope, $http, $location, $routeParams, pagingSvc, editPageSvc){
	
	$scope.base_url = "/api/countries";
	$scope.countries = [];
	$scope.page = Number($routeParams.page);
	$scope.num_pages = 0;
		
	var getCountries = function () {
		$http.get($scope.base_url, { params: {'page': $scope.page}})
			.then(function success(data) {
				$scope.countries = data.data;
				$scope.num_pages = data.headers("pages");
				if ($scope.num_pages === null) {
					$location.path("/countries/page/" + Number($scope.page - 1));
				}
			}, function error(data) {
				console.log(data);
			})
		};
		
	getCountries();
	
	$scope.deleteCountry = function(id) {
		$http.delete($scope.base_url + "/" + id)
			.then(function success(data) {
				getCountries();
		}, function error(data) {
			console.log(data);
		}
	)};
	
	$scope.addCountry = function() {
		$location.path("/countries/add");
	};
	
	$scope.editCountry = function(id, page){
		editPageSvc.storePage(page);
		$location.path("/countries/edit/" + id);
	};
	
	$scope.viewCountry = function(id){
		$location.path("/countries/view/" + id);
	}
	
	$scope.findCountry = function(){
		$location.path("/countries/find");
	};
	
	$scope.search_url = '/countries/page/';
	
	$scope.back = function(){
		pagingSvc.back($scope.search_url, $scope.page);
	};
	
	$scope.forward = function(){
		pagingSvc.forward($scope.search_url, $scope.page);
	};
	
	$scope.first = function(){
		pagingSvc.first($scope.search_url);
	};
	
	$scope.last = function(){
		pagingSvc.last($scope.search_url, $scope.num_pages);
	}
	
});

worldCupApp.controller('addCountryCtrl', function($scope, $http, $location){
	
	$scope.rest_url = '/api/countries';
	$scope.web_address = "/countries/page/"
	$scope.totalPages = 0;
	$scope.country = {};
	
	$scope.add = function(){
		$http.post($scope.rest_url, $scope.country)
			.then(function success(data){
				$scope.totalPages = data.headers("pages");
				$location.path($scope.web_address + ($scope.totalPages - 1));
			}, function error(data){
				console.log(data);
			});
	}
	
});

worldCupApp.controller('findCountryCtrl', function($scope, $http, $location){
	
	$scope.num_pages = 0;
	$scope.page = 0;
	$scope.countrySearch = {};
	$scope.countries = [];
	$scope.indicator = true;
	
	$scope.editCountry = function(id){
		$location.path("/countries/edit/" + id);
	};
	
	$scope.viewCountry = function(id){
		$location.path("/countries/view/" + id);
	}
	
	$scope.deleteCountry = function(id) {
		$http.delete("/api/countries/" + id)
			.then(function success(data) {
				getCountries();
		}, function error(data) {
			console.log(data);
		}
	)};
	
	
	$scope.find = function(){
		getCountries();
	};
	
	$scope.back = function(){
		$scope.page = $scope.page - 1;
		getCountries();
	};
	
	$scope.forward = function(){
//		pagingSvc.forward($scope.search_url, $scope.page);
		$scope.page = $scope.page + 1;
		getCountries();
	};
	
	var getCountries = function(){ 
		
		var config = {params: {}};
		
		config.params.page = $scope.page;
		
		if ($scope.countrySearch.name != "") {
			config.params.name = $scope.countrySearch.name;
		}
		
		$http.get("/api/countries", config)
			.then(function success(data){
				if (data.data != ""){
					$scope.indicator = false;
				} else {
					$scope.indicator = true;
				}
				$scope.countries = data.data;
				$scope.num_pages = data.headers("pages");
			}, function error(data){
				console.log(data);
			});
		
	};
	
});

worldCupApp.controller('editCountryCtrl', function($scope, $http, $routeParams, $location, editPageSvc){
	
	$scope.country = {};
	$scope.rest_url = "/api/countries/";
	$scope.page = editPageSvc.retrievePage();
	
	if (isNaN($scope.page)) {
		$scope.page = 0;
	}
	
	var getCountry = function(id){
		$http.get($scope.rest_url + $routeParams.id)
			.then(function success(data){
				$scope.country = data.data;
			}, function error(data){
				console.log(data);
			})
	};
	
	getCountry();
	
	$scope.edit = function(){
		$http.put($scope.rest_url + $routeParams.id, $scope.country)
			.then(function success(data){
				$location.path("/countries/page/" + $scope.page);
			}, function error(data){
				console.log(data);
			})
		
	};
	
	
});

worldCupApp.controller('viewCountryCtrl', function($scope, $http, $routeParams, $location){
	
	$scope.country = {};
	$scope.rest_url = "/api/countries/";
	
	var getCountry = function(id){
		$http.get($scope.rest_url + $routeParams.id)
		.then(function success(data){
			$scope.country = data.data;
		}, function error(data){
			console.log(data);
		})
	};
	
	getCountry();
	
});


worldCupApp.config(['$routeProvider', function($routeProvider) {
	
	$routeProvider
	
		.when('/', {
			templateUrl : '/static/app/html/partial/home.html' 
		})
		
		.when('/countries/page/:page', {
			templateUrl : '/static/app/html/partial/countries.html'
		})
		
		.when('/countries/add', {
			templateUrl : '/static/app/html/partial/add-country.html'
		})
		.when('/countries/edit/:id', {
			templateUrl : '/static/app/html/partial/edit-country.html'
		})
		
		.when('/countries/find', {
			templateUrl : '/static/app/html/partial/find-country.html'
		})
		
		.when('/countries/view/:id', {
			templateUrl : '/static/app/html/partial/view-country.html'
		})
		
		.when('/worldcups/page/:page', {
			templateUrl : '/static/app/html/partial/worldcups.html'
		})
		
		.when('/worldcups/add', {
			templateUrl : '/static/app/html/partial/add-worldcup.html'
		})
		
		.when('/worldcups/edit/:id', {
			templateUrl : '/static/app/html/partial/edit-worldcup.html'
		})
		
		.when('/worldcups/find', {
			templateUrl : '/static/app/html/partial/find-worldcup.html'
		})
		
		.when('/worldcups/view/:id', {
			templateUrl : '/static/app/html/partial/view-worldcup.html'
		})
		
		.otherwise({
			redirectTo: '/'
		});

}]);