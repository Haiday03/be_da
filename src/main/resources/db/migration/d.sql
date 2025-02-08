create view products_per_manufacturers as
(
select manufacturer_id, count(p.id) as num_prod
from product p
         join manufacturers m
              on m.id = p.manufacturer_id
group by manufacturer_id
    )

