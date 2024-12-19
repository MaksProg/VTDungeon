#!/bin/bash
mkdir lab0
cd lab0
mkdir charmander4 geodude7 quagsire4 kabutops eelektross machamp meditite magby mightyena
mv machamp quagsire4
touch machamp
echo "Развитые способности Steadfast" > machamp
echo "Тип покемона DARK 
FIRE" > houndour
echo "Возможности  Overland=9 Surface=7 Jump=4 Power=4 Intelligence=4 Tracker=0 Stealth=0 Pack Mon=0 Aura=0" > bisharp
echo "Возможности  Overland=2 Surface=1 Jump=2 Power=1 Intelligance=3" > cleffa6
echo "Живёт  Marsh Mountain Urban" > koffing
echo "Развитые Способности 
Poison Touch" > gengar
echo "Ходы  Aqua Tail Body Slam Brine Counter Defense Curl Double-Edge Dive Dynamicpunch Focus Punch Ice Punch Icy Wind Iron Defense Iron Tail Mega Kick Mega Punch Mud-Slap Rollout Seismic Toss Sleep Talk Snore Water Pledge Zen Headbutt" > wartortle
echo "Ходы  Body Slam Counter Defence Curl Doubl-Edge Dynamicpunch Endeavor Fire Punch Hyper Voice Ice Punch Icy Wind Low Kick Mega Kick Mega Punch Mud-Slap Psych Up Rollout Seismic Toss Shock Wave Sleep Talk Snore Thunderpunch Uproar Water Pulse Zen Headbutt" > loudred2
echo "Способности String Shot Tackle Bug Bite Razor Leaf Struggle Bug Endure Bug Buzz Flail" > sewaddle4
mv machamp charmander4
mv kabutops charmander4
mv houndour charmander4
mv bisharp charmander4
mv eelektross geodude7
mv koffing geodude7
mv gengar geodude7
mv wartortle geodude7
mv meditite quagsire4
mv magby quagsire4
mv mightyena quagsire4
#задание 2
chmod u=rwx,g=rw,o=r charmander4
chmod u=rw charmander4/machamp
chmod u=wx,g=rwx charmander4/kabutops
chmod 644 charmander4/houndour
chmod u=r charmander4/bisharp
chmod 600 cleffa6
chmod 570 geodude7
chmod u=wx,g=rw,o=x geodude7/eelektross
chmod 004 geodude7/koffing
chmod 440 geodude7/gengar
chmod u=r,g=r,o=r geodude7/wartortle
chmod 440 loudred2
chmod u=rwx,g=wx,o=wx quagsire4
chmod u=wx,g=rwx,o=wx quagsire4/machamp
chmod u=wx,g=x,o=w quagsire4/meditite
chmod u=rwx,g=wx,o=rwx quagsire4/magby
chmod u=rwx,g=rx,o=w quagsire4/mightyena
chmod 060 sewaddle4
#задание 3
chmod 700 geodude7
ln loudred2 geodude7/gengarloudred
chmod 570 geodude7
cp loudred2 quagsire4/mightyena
chmod 700 charmander4/kabutops
cp -R charmander4 geodude7/eelektross
chmod u=wx,g=rwx charmander4/kabutops
chmod 700 geodude7
cp cleffa6 geodude7/gengarcleffa
ln -s ../sewaddle4 charmander4/machampsewaddle
cat geodude7/wartortle charmander4/houndour > cleffa6_15
ln -s quagsire4 Copy_64
#задание4
echo "----start1----"
cd
ls -R lab0 | grep '4$' | xargs -I{} wc -m lab0/{} | sort -n #1
echo "----start2----"
ls -l lab0/quagsire4 | sort -k2,2nr
echo "----start3----"
grep -i 'e$' lab0/cleffa6 2>/tmp/s467244  #3  
echo "----start4----"
ls -R lab0 2>>/tmp/s467244 | grep ':$' | tr -d ':' | xargs -I{} sh -c 'ls {}/*r 2>>/tmp/s467244' | xargs -I{} sh -c 'cat -n "{}"' 2>>/tmp/s467244
echo "----start5----"
wc -m lab0/quagsire4/*/* | sort -nr
echo "----start6----"
ls -ltuR lab0 | grep '^-' | sort -k6,7 | tail -n 2 #6-ой пункт
echo "----end----"
#задание 5
cd lab0
rm -f sewaddle4
rm charmander4/machamp
rm charmander4/machampsewadd*
rm -f geodude7/gengarloudr*

