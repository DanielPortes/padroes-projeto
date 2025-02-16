#!/bin/bash

# Configura o script para encerrar em caso de erro
set -e

# Solicita o nome do exercício ou pasta
read -p "Digite o nome do exercício ou pasta (exemplo: exercicio01): " EXERCICIO

# Define o destino do repositório
DEST="../${EXERCICIO}-repo"

# Cria diretórios necessários
echo "Criando estrutura de diretórios em $DEST..."
mkdir -p "$DEST/src/main/java/padroescriacao"
mkdir -p "$DEST/src/test/java/padroescriacao"

# Copia arquivos essenciais com validação
echo "Copiando arquivos necessários..."
[ -f .gitignore ] && cp .gitignore "$DEST/" || echo "Aviso: .gitignore não encontrado."
[ -f pom.xml ] && cp pom.xml "$DEST/" || echo "Aviso: pom.xml não encontrado."
[ -f README.md ] && cp README.md "$DEST/" || echo "Aviso: README.md não encontrado."
[ -f "./diagramas/diagrama_${EXERCICIO}.png" ] && cp "./diagramas/diagrama_${EXERCICIO}.png" "$DEST/" || echo "Aviso: Diagrama não encontrado."

# Copia os diretórios do exercício, se existirem
if [ -d "src/main/java/padroescriacao/${EXERCICIO}" ]; then
    cp -r "src/main/java/padroescriacao/${EXERCICIO}" "$DEST/src/main/java/padroescriacao/"
else
    echo "Aviso: Código fonte não encontrado para $EXERCICIO."
fi

if [ -d "src/test/java/padroescriacao/${EXERCICIO}" ]; then
    cp -r "src/test/java/padroescriacao/${EXERCICIO}" "$DEST/src/test/java/padroescriacao/"
else
    echo "Aviso: Testes não encontrados para $EXERCICIO."
fi

# Inicializa o repositório Git
echo "Inicializando o repositório Git..."
cd "$DEST" || exit
git init

# Cria ou sobrescreve o README.md
cat <<EOF > README.md
# Padrões de Projeto

## $EXERCICIO
![diagrama_${EXERCICIO}.png](diagrama_${EXERCICIO}.png)
EOF

# Cria o commit inicial
echo "Criando commit inicial..."
TODAY=$(date +'%Y-%m-%d')
#OLD_COMMIT_DATE=$(date -R -d "10 years ago")
GIT_COMMITTER_DATE="$TODAY" git commit --allow-empty -m "Iniciando exercício $EXERCICIO"
git add .
git commit -m "Adicionando arquivos para $EXERCICIO"

# Solicita a URL do repositório remoto
read -p "Digite a URL do repositório remoto: " URL

# Adiciona o repositório remoto e faz o push
git remote add origin "$URL"
git branch -M main
git push --force origin main

echo "Repositório remoto configurado com sucesso em $URL."

# Limpa diretório temporário
echo "Limpando diretório temporário..."
cd ..
rm -rf "$DEST"

echo "Repositório criado e enviado com sucesso!"
