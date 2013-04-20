#!/usr/bin/env ruby

require 'whole_history_rating'

# w = 0 for static ratings aka bayeselo
@whr = WholeHistoryRating::Base.new(:w2 => 0)

# WholeHistoryRating::Base#create_game arguments: black player name, white player name, winner, day number, handicap
# Handicap should generally be less than 500 elo
ARGF.each do |line|
    tokens = line.split(' ')
    @whr.create_game(tokens[0], tokens[1], tokens[2], 0, 0)
end

# Iterate the WHR algorithm towards convergence with more players/games, more iterations are needed.
@whr.iterate(1000)

players = @whr.players.values.select {|p| p.days.count > 0}
players.sort_by { |p| p.days.last.gamma }.each_with_index do |p,idx|
    if p.days.count > 0
      puts "#{p.name}\t#{p.days[0].gamma}\t#{p.days[0].uncertainty}"
    end
end
