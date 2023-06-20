insert into country(id, name,country_code) values(1, 'France','fr');
insert into country(id, name,country_code) values(2, 'England','en');
insert into country(id, name,country_code) values(3, 'Germany','de');
insert into country(id, name,country_code) values(4, 'Spain','es');

insert into town(id, country_id, name) values (1, 1, 'TEST');
insert into town(id, country_id, name) values (2, 1, 'Poitiers');
insert into url(id, town_id, title, url_string) values (1, 2, 'car park location', 'https://data.grandpoitiers.fr/api/records/1.0/search/?dataset=mobilite-parkings-grand-poitiers-donnees-metiers&rows=1000&facet=nom_du_parking&facet=zone_tarifaire&facet=statut2&facet=statut3');
insert into url(id, town_id, title, url_string) values (2, 2, 'car park information', 'https://data.grandpoitiers.fr/api/records/1.0/search/?dataset=mobilites-stationnement-des-parkings-en-temps-reel&facet=nom');
insert into town_urls(town_id, urls_id) values (2,1);
insert into town_urls(town_id, urls_id) values (2,2);

