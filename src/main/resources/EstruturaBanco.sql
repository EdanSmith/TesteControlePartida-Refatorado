CREATE DATABASE  IF NOT EXISTS `teste_keep_it_simple` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;
USE `teste_keep_it_simple`;

--
-- Table structure for table `jogador`
--

DROP TABLE IF EXISTS `jogador`;

CREATE TABLE `jogador` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nome_UNIQUE` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Table structure for table `partida`
--

DROP TABLE IF EXISTS `partida`;

CREATE TABLE `partida` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `numero_controle` int(11) NOT NULL,
  `inicio` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `fim` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `numero_controle_UNIQUE` (`numero_controle`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


--
-- Table structure for table `partida_detalhe`
--

DROP TABLE IF EXISTS `partida_detalhe`;

CREATE TABLE `partida_detalhe` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_partida` int(11) NOT NULL,
  `killtime` time NOT NULL,
  `id_jogador_killer` int(11) NOT NULL,
  `id_jogador_killed` int(11) NOT NULL,
  `id_weapon` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_partida_fk2_idx` (`id_partida`),
  KEY `id_jogador_fk_killer_idx` (`id_jogador_killer`),
  KEY `id_jogador_fk_killed_idx` (`id_jogador_killed`),
  KEY `id_weapon_fk_idx` (`id_weapon`),
  CONSTRAINT `id_jogador_fk_killed` FOREIGN KEY (`id_jogador_killed`) REFERENCES `jogador` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `id_jogador_fk_killer` FOREIGN KEY (`id_jogador_killer`) REFERENCES `jogador` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `id_partida_fk_partida` FOREIGN KEY (`id_partida`) REFERENCES `partida` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `id_weapon_fk` FOREIGN KEY (`id_weapon`) REFERENCES `weapon` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Table structure for table `partida_jogador`
--

DROP TABLE IF EXISTS `partida_jogador`;

CREATE TABLE `partida_jogador` (
  `id_partida` int(11) NOT NULL,
  `id_jogador` int(11) NOT NULL,
  KEY `id_partida_fk_idx` (`id_partida`),
  KEY `id_jogador_fk_idx` (`id_jogador`),
  CONSTRAINT `id_jogador_fk` FOREIGN KEY (`id_jogador`) REFERENCES `jogador` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `id_partida_fk` FOREIGN KEY (`id_partida`) REFERENCES `partida` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


--
-- Table structure for table `weapon`
--

DROP TABLE IF EXISTS `weapon`;

CREATE TABLE `weapon` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;