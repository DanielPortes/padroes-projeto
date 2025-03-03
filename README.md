# Padrões de Projeto em Java

[![Build Status](https://github.com/SEU_USUARIO_GITHUB/padroes-projeto/actions/workflows/main.yml/badge.svg)](https://github.com/SEU_USUARIO_GITHUB/padroes-projeto/actions/workflows/main.yml)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Este repositório contém implementações de padrões de projeto em Java, desenvolvidos como parte de estudos e exercícios sobre padrões de design de software.

## Estrutura do Projeto

O projeto está organizado em diferentes categorias de padrões de design:

- **Criacionais**: Padrões focados na criação de objetos
- **Estruturais**: Padrões focados na composição de classes e objetos
- **Comportamentais**: Padrões focados na comunicação entre objetos

Além disso, o projeto inclui um trabalho final que integra diversos padrões em um sistema de automação residencial.

## Padrões Implementados

### Padrões Criacionais
- Singleton
- Factory Method
- Abstract Factory
- Builder
- Prototype

### Padrões Estruturais
- Adapter
- Bridge
- Composite
- Decorator
- Facade
- Flyweight
- Proxy

### Padrões Comportamentais
- Chain of Responsibility
- Command
- Interpreter
- Iterator
- Mediator
- Memento
- Observer
- State
- Strategy
- Template Method
- Visitor

## Trabalho Final - Sistema de Automação Residencial

O trabalho final implementa um sistema de automação residencial que integra múltiplos padrões de design em um cenário coeso. O sistema permite:

- Controle de dispositivos inteligentes como luzes e termostatos
- Gestão de cômodos da casa
- Aplicação de estratégias de economia de energia
- Configuração de rotinas automatizadas
- Sistema de segurança

### Padrões Utilizados no Trabalho Final

- **Singleton**: Classes centrais do sistema como `HomeCentral`
- **Abstract Factory**: Criação de dispositivos de diferentes fabricantes
- **Bridge**: Separação entre dispositivos e suas implementações
- **Command**: Execução de comandos em dispositivos e salas
- **Observer**: Sistema de notificações e eventos
- **State**: Gerenciamento de estados dos dispositivos
- **Strategy**: Estratégias de gerenciamento de energia
- **Template Method**: Rotinas de automação
- **Decorator**: Adicionar funcionalidades aos dispositivos
- **Mediator**: Comunicação entre subsistemas
- **Flyweight**: Compartilhamento de atributos de cômodos
- **Visitor**: Relatórios e diagnósticos do sistema

## Como Executar

### Pré-requisitos
- JDK 20 ou superior
- Maven

### Compilação
```bash
mvn clean compile
```

### Execução de Testes
```bash
mvn test
```

### Execução do Trabalho Final
```bash
mvn exec:java -Dexec.mainClass="trabalhofinal.smarthome.Main"
```

## Ferramentas de Desenvolvimento

- **IDE**: IntelliJ IDEA, Eclipse ou VSCode
- **Construção**: Maven
- **Testes**: JUnit 5
- **Controle de Versão**: Git
- **CI/CD**: GitHub Actions

## Scripts de Suporte

O projeto inclui scripts para facilitar o desenvolvimento:

- `separar-exercicio.sh`: Extrai exercícios específicos em repositórios separados


## Licença

Este projeto está licenciado sob a [Licença MIT](LICENSE).

## Autor

Daniel Fagundes