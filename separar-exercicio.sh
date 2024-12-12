#!/bin/bash

echo "Digite o nome do exercício ou pasta (exemplo: exercicio01): "
read EXERCICIO

DEST=../$EXERCICIO-repo

# Criar diretório temporário
mkdir -p $DEST/src/main/java/padroescriacao
mkdir -p $DEST/src/test/java/padroescriacao

# Copiar arquivos necessários
cp .gitignore $DEST/
cp pom.xml $DEST/
cp README.md $DEST/
cp ./diagramas/diagrama_$EXERCICIO.png $DEST/
cp -r src/main/java/padroescriacao/$EXERCICIO $DEST/src/main/java/padroescriacao/
cp -r src/test/java/padroescriacao/$EXERCICIO $DEST/src/test/java/padroescriacao/

# Inicializar repositório
cd $DEST

# create a readme file
echo "# Padrões de projeto
 ____
  ## $EXERCICIO<br><br>![diagrama_$EXERCICIO.png](diagrama_exercicio01.png)" > README.md

git init
git add .
git commit -m "Isto eh um commit automatico $EXERCICIO"
git branch -M main

# read url input from user
echo "Digite a URL do repositório remoto: "
read url

# Adicionar repositório remoto
git remote add origin $url
git push --force origin main

echo "Repositório criado com sucesso"
echo "Repositório remoto adicionado com sucesso em $url"
