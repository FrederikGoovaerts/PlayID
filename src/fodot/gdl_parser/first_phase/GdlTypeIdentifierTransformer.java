package fodot.gdl_parser.first_phase;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import fodot.util.GdlClassCorrectionUtil;
import org.ggp.base.util.gdl.grammar.GdlConstant;
import org.ggp.base.util.gdl.grammar.GdlDistinct;
import org.ggp.base.util.gdl.grammar.GdlFunction;
import org.ggp.base.util.gdl.grammar.GdlLiteral;
import org.ggp.base.util.gdl.grammar.GdlNot;
import org.ggp.base.util.gdl.grammar.GdlOr;
import org.ggp.base.util.gdl.grammar.GdlPool;
import org.ggp.base.util.gdl.grammar.GdlProposition;
import org.ggp.base.util.gdl.grammar.GdlRelation;
import org.ggp.base.util.gdl.grammar.GdlRule;
import org.ggp.base.util.gdl.grammar.GdlSentence;
import org.ggp.base.util.gdl.grammar.GdlTerm;
import org.ggp.base.util.gdl.grammar.GdlVariable;

import fodot.exceptions.gdl.GdlTypeIdentificationError;
import fodot.gdl_parser.GdlTransformer;
import fodot.gdl_parser.first_phase.data.declarations.GdlFunctionDeclaration;
import fodot.gdl_parser.first_phase.data.declarations.GdlPredicateDeclaration;
import fodot.gdl_parser.first_phase.data.declarations.IGdlArgumentListDeclaration;

/**
 * This class will be used to visit the relations and rules.
 * It will use the GdlRuleElementsVisitor to visit the rules deeper.
 * @author Thomas Winters
 */
class GdlTypeIdentifierTransformer implements GdlTransformer {

	private GdlTypeIdentifier identifier;

	public GdlTypeIdentifierTransformer(GdlTypeIdentifier argIdentifier) {
		this.identifier = argIdentifier;
	}

	public GdlTypeIdentifier getIdentifier() {
		return identifier;
	}

	/**********************************************
	 *  Relation processing: only for type defining.
	 *  Most of the time, you won't add the occurrence
	 *  because init, legal etc are not part of our GDL file
	 ***********************************************/

	@Override
	public void processRoleRelation(GdlRelation relation) {
		visitArguments(null, relation.getBody(), getIdentifier().getRole());
	}

	@Override
	public void processInitRelation(GdlRelation relation) {

        GdlTerm argument = relation.get(0);

        if (argument instanceof GdlConstant) {
            GdlProposition argumentProposition = GdlClassCorrectionUtil.convertToProposition((GdlConstant) argument);

            identifier.registerProposition(argumentProposition);
        } else if (argument instanceof GdlFunction) {
            GdlRelation argumentRelation = GdlClassCorrectionUtil.convertToPredicate(argument);

            //Visit argument
            getIdentifier().addPredicateOccurrence(null, argumentRelation);
            visitPredicateArguments(null, argumentRelation);

            //Make argument dynamic
            getIdentifier().registerDynamicPredicate(argumentRelation);
        } else {
            throw new GdlTypeIdentificationError("Argument was neither Constant nor Function!");
        }
    }

	@Override
	public void processStaticPredicateRelation(GdlRelation relation) {
		getIdentifier().addPredicateOccurrence(null, relation);
		visitPredicateArguments(null, relation);
	}

	@Override
	public void processLegalRelation(GdlRelation relation) {
		visitArguments(null, relation.getBody(), getIdentifier().getLegal());
	}

	/**********************************************/

	/**********************************************
	 *  Rule processing
	 ***********************************************/
	@Override
	public void processNextRule(GdlRule rule) {
		GdlRelation argumentRelation = GdlClassCorrectionUtil.convertToPredicate(rule.getHead().get(0));

		//Visit argument
		getIdentifier().addPredicateOccurrence(rule, argumentRelation);
		visitPredicateArguments(rule, argumentRelation);

		//Make argument dynamic
		getIdentifier().registerDynamicPredicate(argumentRelation);

		visitRuleBody(rule);
	}

	@Override
	public void processLegalRule(GdlRule rule) {
		visitRule(rule);
	}

	@Override
	public void processGoalRule(GdlRule rule) {
		visitRule(rule);
	}

	@Override
	public void processTerminalRule(GdlRule rule) {
		assert rule.getHead().arity() == 0;
		visitRule(rule);
	}

	@Override
	public void processDefinitionRule(GdlRule rule) {
		visitRule(rule);
	}		


	//Helper
	private void visitRule(GdlRule rule) {
		GdlRelation relation = GdlClassCorrectionUtil.convertToPredicate(rule.getHead());
		//If the head is dynamic, then so is the rule
		if (getIdentifier().isDynamicPredicate(relation)) {
			getIdentifier().registerTimeDependent(rule);
		}
		
		getIdentifier().addPredicateOccurrence(rule, relation);
		visitPredicateArguments(rule, relation);
		visitRuleBody(rule);
	
		//If the body turned out to be dynamic, then so is the head
		if (getIdentifier().isTimeDependentRule(rule)) {
			getIdentifier().registerDynamicPredicate(relation);
		}
		
	}
	
	private void visitRuleBody(GdlRule rule) {
		GdlRuleBodyVisitor bodyVisitor = new GdlRuleBodyVisitor(rule);
		bodyVisitor.visitBodyElements();
	}
	/**********************************************/

	private class GdlRuleBodyVisitor {
		//We will need this rule so we can tell what variables are the same.
		private GdlRule rule;

		public GdlRuleBodyVisitor(GdlRule rule) {
			this.rule = rule;
		}

		public void visitBodyElements() {	
			visitLiterals(rule.getBody());
		}

		private void visitLiterals(Collection<? extends GdlLiteral> literals) {
			for (GdlLiteral lit : literals) {
				visitLiteral(lit);
			}	

		}

		private void visitLiteral(GdlLiteral lit) {
			if (lit instanceof GdlDistinct) {
				visitDistinct( (GdlDistinct) lit);
			} else if (lit instanceof GdlNot) {
				visitNot( (GdlNot) lit);
			} else if (lit instanceof GdlOr) {
				visitOr( (GdlOr) lit);
			} else if (lit instanceof GdlSentence) {
				visitSentence( (GdlSentence) lit);
			} else {
				throw new GdlTypeIdentificationError();
			}
		}

		//Basic literals
		private void visitDistinct(GdlDistinct distinct) {
			visitArguments(rule, Arrays.asList(distinct.getArg1(), distinct.getArg2()), getIdentifier().getDistinct());
		}		
		public void visitNot(GdlNot not) {
			visitLiteral(not.getBody());
		}
		public void visitOr(GdlOr or) {
			visitLiterals(or.getDisjuncts());
		}

		//Sentences
		public void visitSentence(GdlSentence sentence) {
			if (sentence instanceof GdlRelation) {
				visitRelation( (GdlRelation) sentence);
			} else if (sentence instanceof GdlProposition) {
				visitProposition( (GdlProposition) sentence);
			} else {
				throw new GdlTypeIdentificationError();
			}
		}
		private void visitRelation(GdlRelation predicate) {
			if (getIdentifier().getTrue().equals(new GdlPredicateDeclaration(predicate))) {
				visitTrue(predicate);
			} else {
				getIdentifier().addPredicateOccurrence(rule, predicate);
				visitPredicateArguments(rule, predicate);
				if (getIdentifier().isDynamicPredicate(predicate)) {
					getIdentifier().registerTimeDependent(rule);
				}
			}
		}
		private void visitTrue(GdlRelation predicate) {

            if (predicate.arity() == 0) {
                GdlProposition argumentProposition = GdlClassCorrectionUtil.convertToProposition(predicate);

                identifier.registerProposition(argumentProposition);
            } else {
                GdlRelation innerPredicate = GdlClassCorrectionUtil.convertToPredicate(predicate.get(0));
                visitRelation(innerPredicate);
                //TODO FIX PROPOSITION

                getIdentifier().registerDynamicPredicate(innerPredicate);
                getIdentifier().registerTimeDependent(rule);
            }
		}

		private void visitProposition(GdlProposition proposition) {
			//Propositions are predicates without arguments.
			//Hypothese: These are always dynamic, as they don't serve a better purpose
            identifier.registerProposition(proposition);
		}

	}


	/**********************************************
	 *  Inner elemens visitor
	 ***********************************************/
	public void visitArguments(GdlRule rule, List<GdlTerm> terms, IGdlArgumentListDeclaration parent) {
		for (int i = 0; i < terms.size(); i++) {
			GdlTerm term = terms.get(i);
			if (term instanceof GdlConstant) {
				getIdentifier().addConstantOccurrence(parent, i, (GdlConstant) term);
			} else if (term instanceof GdlVariable) {
				getIdentifier().addVariableOccurrence(rule, parent, i, (GdlVariable) term);
			} else if (term instanceof GdlFunction) {
				GdlFunction func = (GdlFunction) term;
				getIdentifier().addFunctionOccurrence(rule, parent, i, func);
				visitFunctionArguments(rule, func);
			} else {
				throw new GdlTypeIdentificationError();
			}
		}
	}

	public void visitPredicateArguments(GdlRule rule, GdlRelation predicate) {
		this.visitArguments(rule, predicate.getBody(), new GdlPredicateDeclaration(predicate) );
	}

	public void visitFunctionArguments(GdlRule rule, GdlFunction function) {
		this.visitArguments(rule, function.getBody(), new GdlFunctionDeclaration(function) );
	}


}