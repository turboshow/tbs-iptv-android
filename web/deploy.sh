#! /bin/sh
yarn install & yarn build

mv build www

zip -r www.zip www

mv www.zip ../app/src/main/assets

rm -rf www