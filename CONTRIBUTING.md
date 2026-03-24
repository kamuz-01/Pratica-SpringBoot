# Contributing Guide

Obrigado por contribuir com este projeto.

## Antes de começar

- Verifique se o código atual já cobre o que você quer alterar.
- Evite mudanças amplas sem necessidade.
- Preserve o padrão atual de pacotes e nomes.

## Fluxo sugerido

1. Crie uma branch para a alteração.
2. Faça a mudança com foco em um único objetivo.
3. Teste localmente.
4. Verifique se não houve regressões.
5. Abra a pull request com uma descrição clara.

## Padrões do projeto

- Use Java 21.
- Mantenha os pacotes dentro de `org.Pratica_SpringBoot`.
- Prefira nomes descritivos para classes, métodos e variáveis.
- Siga o padrão existente de controllers, services e repositories.
- Não misture regra de negócio com controller.

## API

- Ao criar endpoints com multipart, documente claramente os nomes das partes.
- Se adicionar novos erros, trate-os no handler global da aplicação.
- Sempre valide os dados de entrada com annotations de validação quando fizer sentido.

## Testes e validação

Antes de enviar uma contribuição:

- rode a aplicação localmente
- valide os endpoints alterados
- confirme que a compilação continua funcionando
- revise logs e respostas de erro

## Estilo de commit

Prefira mensagens curtas e objetivas, por exemplo:

- Ajusta validação de professor
- Corrige upload de imagem
- Melhora tratamento de erro

## O que evitar

- mudanças sem relação com a tarefa
- renomear pacotes sem necessidade
- remover tratamento de erro existente sem substituição
- subir arquivos gerados ou temporários

## Dúvidas

Se uma alteração mexer no contrato da API, documente o impacto no README antes de finalizar.
