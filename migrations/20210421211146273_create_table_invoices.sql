CREATE TABLE IF NOT EXISTS `invoices` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT,
     `owner_id` bigint(20) NOT NULL,
     `status` varchar(255) NOT NULL,
     `created_at` datetime NOT NULL DEFAULT now(),
     `authorized_at` datetime NULL,

     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE INDEX `idx_id_owner` ON `invoices` (`id`, `owner_id`);