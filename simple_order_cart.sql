-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 28, 2024 at 03:38 PM
-- Server version: 10.4.25-MariaDB
-- PHP Version: 8.0.23

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `simple_order_cart`
--

-- --------------------------------------------------------

--
-- Table structure for table `order_carts`
--

CREATE TABLE `order_carts` (
  `order_cart_id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `customer_address` text DEFAULT NULL,
  `customer_name` varchar(50) NOT NULL,
  `ordered_at` timestamp NULL DEFAULT NULL,
  `total` double NOT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `updated_by` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `order_details`
--

CREATE TABLE `order_details` (
  `order_cart_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `quantity` int(11) NOT NULL,
  `total` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `product_id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `name` varchar(255) NOT NULL,
  `price` decimal(10,0) NOT NULL,
  `stock` int(11) NOT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `product_type_id` bigint(20) NOT NULL,
  `updated_by` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`product_id`, `created_at`, `name`, `price`, `stock`, `updated_at`, `created_by`, `product_type_id`, `updated_by`) VALUES
(1, '2024-05-28 13:26:33', 'Macbook Pro', '15000000', 88, NULL, 1, 1, NULL),
(2, '2024-05-28 13:27:26', 'Head First Java', '350000', 25, NULL, 1, 2, NULL),
(3, '2024-05-28 13:27:53', 'Professional Apache Tomcat 8', '30000', 25, NULL, 1, 2, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `product_types`
--

CREATE TABLE `product_types` (
  `product_type_id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `product_types`
--

INSERT INTO `product_types` (`product_type_id`, `name`) VALUES
(1, 'Laptop'),
(2, 'Book');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `token_expired_at` bigint(20) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `name`, `password`, `token`, `token_expired_at`, `username`) VALUES
(1, 'user tester 1', '$2a$10$ESZfEC8Yoy5ygArpO2lz9uZJT3AK9nSBohPEN4xxXej8fZ4pGzWHS', 'f8bbbb0c-6cf9-4445-942f-f9737da26b6e', 1719494676024, 'tester1'),
(2, 'user tester 2', '$2a$10$fUeTYOP/Ms6iOS3sTus23OShFAjqD2LUmDn5wCwMxq9Zerol7EMFy', NULL, NULL, 'tester2');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `order_carts`
--
ALTER TABLE `order_carts`
  ADD PRIMARY KEY (`order_cart_id`),
  ADD KEY `FKoeamj0dwq2o59fy8fpm4efy96` (`created_by`),
  ADD KEY `FKpq5u2tiqed2hbv7rt1p2rwr1v` (`updated_by`);

--
-- Indexes for table `order_details`
--
ALTER TABLE `order_details`
  ADD PRIMARY KEY (`order_cart_id`,`product_id`),
  ADD KEY `FK4q98utpd73imf4yhttm3w0eax` (`product_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`),
  ADD KEY `FKl0lce8i162ldn9n01t2a6lcix` (`created_by`),
  ADD KEY `FKrv6og3b2qlahvka0bxn7btyqd` (`product_type_id`),
  ADD KEY `FKdeswm6d74skv6do803axl6edj` (`updated_by`);

--
-- Indexes for table `product_types`
--
ALTER TABLE `product_types`
  ADD PRIMARY KEY (`product_type_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `UK_af44yue4uh61eqwe6jjyqwvla` (`token`),
  ADD UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `order_carts`
--
ALTER TABLE `order_carts`
  MODIFY `order_cart_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `product_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `product_types`
--
ALTER TABLE `product_types`
  MODIFY `product_type_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `order_carts`
--
ALTER TABLE `order_carts`
  ADD CONSTRAINT `FKoeamj0dwq2o59fy8fpm4efy96` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `FKpq5u2tiqed2hbv7rt1p2rwr1v` FOREIGN KEY (`updated_by`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `order_details`
--
ALTER TABLE `order_details`
  ADD CONSTRAINT `FK4q98utpd73imf4yhttm3w0eax` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`),
  ADD CONSTRAINT `FKjol45tqasbb56cog3wtwjtahn` FOREIGN KEY (`order_cart_id`) REFERENCES `order_carts` (`order_cart_id`);

--
-- Constraints for table `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `FKdeswm6d74skv6do803axl6edj` FOREIGN KEY (`updated_by`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `FKl0lce8i162ldn9n01t2a6lcix` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `FKrv6og3b2qlahvka0bxn7btyqd` FOREIGN KEY (`product_type_id`) REFERENCES `product_types` (`product_type_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
