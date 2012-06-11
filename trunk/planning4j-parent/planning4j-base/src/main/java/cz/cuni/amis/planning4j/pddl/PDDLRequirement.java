/*
 * Copyright (C) 2011 AMIS research group, Faculty of Mathematics and Physics, Charles University in Prague, Czech Republic
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.amis.planning4j.pddl;

/**
 * Enum representing possible PDDL requirements.
 * 
 * For a list of PDDL requirements with comments see
 * https://cs05.informatik.uni-ulm.de/ki/Edu/Vorlesungen/GdKI/WS0203/pddl.pdf
 * Section 15.
 * @author Martin Cerny
 */
public enum PDDLRequirement {
    //TODO full list of requirements
    STRIPS("strips"), 
    TYPING("typing"), 
    DISJUNCTIVE_PRECONDITIONS("disjunctive-preconditions"),
    EQUAILTY("equality"), 
    EXISTENTIAL_PRECONDITIONS("existential-preconditions"),    
    UNIVERSAL_PRECONDITIONS("universal-preconditions"),    
    QUANTIFIED_PRECONDITIONS("quantified-preconditions"),    
    CONDITIONAL_EFFECTS("conditional-effects"),
    ACTION_EXPANSIONS("action-expansions"),
    FOREACH_EXPANSIONS("foreach-expansions"),
    DAG_EXPANSIONS("dag-expansions"),
    DOMAIN_AXIOMS("domain-axioms"),
    SUBGOAL_THROUGHT_AXIOMS("subgoal-through-axioms"),
    SAFETY_CONSTRAINTS("safety-constraints"),
    EXPRESSION_EVALUAION("expression-evaluation"),
    FLUENTS("fluents"),
    OPEN_WORLD("open-world"),
    TRUE_NEGATION("true-negation"),
    ADL("adl"),
    UCPOP("ucpop"),
    
    //Some newer preconditions I can not find a useful documenation link for
    MDP("mdp"),
    REWARDS("rewards"),
    CONTINUOUS_EFFECTS("continuous-effects"),
    DURATION_INEQUALITES("duration-inequalities"),
    DURATIVE_ACTIONS("durative-actions"),
    NEGATIVE_PRECONDITIONS("negative-preconditions"),
    NUMERIC_FLUENTS("numeric-fluents"),
    PROBABILISTIC_EFFECTS("probabilistic-effects"),
    CONSTRAINTS("constraints"),
    OBJECT_FLUENTS("object-fluents"),
    PREFERENCES("preferences"),
    TIMED_INITIAL_LITERALS("timed-initial-literals")
    
    ;
    /**
     * Name in pddl (without leading colon)
     */
    String pddlName;

    private PDDLRequirement(String pddlName) {
        this.pddlName = pddlName;
    }

    public String getPddlName() {
        return pddlName;
    }
    
}
