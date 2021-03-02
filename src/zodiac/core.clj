(ns zodiac.core
  (:gen-class)
  (:require [java-time :as jt]))

(defn date-in? [from to target-date]
  (and (jt/after? target-date from) (jt/before? target-date to)))

(defn convert-day-to-date [base-date day]
  (-> (jt/year base-date) (str "-" day) (jt/local-date)))

(defn is-zodiac? [target-date [from-date to-date zodiac-name]]
  (let [from (convert-day-to-date target-date from-date)
        to (convert-day-to-date target-date to-date)]
    (if (jt/after? (jt/month from) (jt/month to))
      (if (= (jt/month from) (jt/month target-date))
        (when (date-in? from (jt/plus to (jt/years 1)) target-date)
          zodiac-name)
        (when (date-in? (jt/minus from (jt/years 1)) to target-date)
          zodiac-name))
      (when (date-in? from to target-date)
        zodiac-name))))

(def zodiac-table
  [["03-20" "04-20" :Aries]
   ["04-19" "05-21" :Taurus]
   ["05-20" "06-22" :Gemini]
   ["06-21" "07-23" :Cancer]
   ["07-22" "08-23" :Leo]
   ["08-22" "09-23" :Virgo]
   ["09-22" "10-24" :Libra]
   ["10-23" "11-24" :Scorpio]
   ["11-22" "12-22" :Sagittarius]
   ["12-21" "01-20" :Capricorn]
   ["01-19" "02-19" :Aquarius]
   ["02-18" "03-21" :Pisces]])

(defn zodiac
  "return zodiac name on argument date."
  [date]
  (let [target-day (jt/local-date date)]
    (first (remove nil? (map #(is-zodiac? target-day %) zodiac-table)))))


(defn -main
  "usage: program yyyy-mm-dd"
  [& args]
  (if (< 0 (count args))
    (println (zodiac (first args)))
    (println "Usage: program yyyy-mm-dd")))
