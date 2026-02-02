import { ArrowLeft, Award, Medal, Star, Trophy, Sparkles, Target, Zap, Flame, TrendingUp, Heart, Leaf, Package, Clock, Gift } from 'lucide-react';
import { Card } from './ui/card';
import { Badge } from './ui/badge';
import { Progress } from './ui/progress';

interface AchievementsProps {
  userName: string;
  onBack: () => void;
}

interface Achievement {
  id: string;
  title: string;
  description: string;
  icon: React.ElementType;
  progress: number;
  maxProgress: number;
  unlocked: boolean;
  points: number;
  rarity: 'common' | 'rare' | 'epic' | 'legendary';
}

export function Achievements({ userName, onBack }: AchievementsProps) {
  const achievements: Achievement[] = [
    {
      id: '1',
      title: 'Primer Paso',
      description: 'Recicla tu primer elemento',
      icon: Star,
      progress: 1,
      maxProgress: 1,
      unlocked: true,
      points: 50,
      rarity: 'common'
    },
    {
      id: '2',
      title: 'Amante del Plástico',
      description: 'Recicla 5 elementos de plástico',
      icon: Package,
      progress: 3,
      maxProgress: 5,
      unlocked: false,
      points: 100,
      rarity: 'common'
    },
    {
      id: '3',
      title: 'Velocista Verde',
      description: 'Recicla 3 elementos en menos de 24 horas',
      icon: Zap,
      progress: 3,
      maxProgress: 3,
      unlocked: true,
      points: 150,
      rarity: 'rare'
    },
    {
      id: '4',
      title: 'Racha de Fuego',
      description: 'Mantén una racha de 7 días consecutivos',
      icon: Flame,
      progress: 4,
      maxProgress: 7,
      unlocked: false,
      points: 200,
      rarity: 'rare'
    },
    {
      id: '5',
      title: 'Maestro Reciclador',
      description: 'Recicla 50 elementos en total',
      icon: Trophy,
      progress: 24,
      maxProgress: 50,
      unlocked: false,
      points: 300,
      rarity: 'epic'
    },
    {
      id: '6',
      title: 'Eco Guerrero',
      description: 'Alcanza 1000 puntos totales',
      icon: Target,
      progress: 1250,
      maxProgress: 1000,
      unlocked: true,
      points: 250,
      rarity: 'epic'
    },
    {
      id: '7',
      title: 'Madrugador Verde',
      description: 'Recicla antes de las 8 AM',
      icon: Clock,
      progress: 0,
      maxProgress: 1,
      unlocked: false,
      points: 75,
      rarity: 'common'
    },
    {
      id: '8',
      title: 'Guardián del Planeta',
      description: 'Recicla 100kg de materiales',
      icon: Heart,
      progress: 5.2,
      maxProgress: 100,
      unlocked: false,
      points: 500,
      rarity: 'legendary'
    },
    {
      id: '9',
      title: 'Influencer Eco',
      description: 'Comparte tu progreso en redes sociales',
      icon: TrendingUp,
      progress: 0,
      maxProgress: 1,
      unlocked: false,
      points: 100,
      rarity: 'rare'
    },
    {
      id: '10',
      title: 'Coleccionista',
      description: 'Desbloquea 5 logros',
      icon: Gift,
      progress: 3,
      maxProgress: 5,
      unlocked: false,
      points: 200,
      rarity: 'epic'
    }
  ];

  const unlockedCount = achievements.filter(a => a.unlocked).length;
  const totalPoints = achievements.filter(a => a.unlocked).reduce((sum, a) => sum + a.points, 0);

  const getRarityColor = (rarity: Achievement['rarity']) => {
    switch (rarity) {
      case 'common': return 'from-gray-400 to-gray-600';
      case 'rare': return 'from-blue-400 to-blue-600';
      case 'epic': return 'from-purple-400 to-purple-600';
      case 'legendary': return 'from-amber-400 to-yellow-600';
    }
  };

  const getRarityBadgeColor = (rarity: Achievement['rarity']) => {
    switch (rarity) {
      case 'common': return 'bg-gray-100 text-gray-700';
      case 'rare': return 'bg-blue-100 text-blue-700';
      case 'epic': return 'bg-purple-100 text-purple-700';
      case 'legendary': return 'bg-yellow-100 text-yellow-700';
    }
  };

  const getRarityName = (rarity: Achievement['rarity']) => {
    switch (rarity) {
      case 'common': return 'Común';
      case 'rare': return 'Raro';
      case 'epic': return 'Épico';
      case 'legendary': return 'Legendario';
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-b from-amber-50 via-yellow-50 to-white pb-20">
      {/* Header */}
      <div className="bg-gradient-to-r from-amber-500 to-yellow-600 pt-12 pb-8 px-6 rounded-b-3xl shadow-lg mb-6">
        <div className="flex items-center mb-6">
          <button 
            onClick={onBack}
            className="bg-white/20 backdrop-blur-sm p-3 rounded-full hover:bg-white/30 transition-all shadow-md mr-4"
          >
            <ArrowLeft className="w-6 h-6 text-white" />
          </button>
          <div className="flex-1">
            <h1 className="text-white mb-1">Mis Logros</h1>
            <p className="text-amber-100">¡Sigue desbloqueando más!</p>
          </div>
        </div>

        {/* Resumen */}
        <div className="grid grid-cols-2 gap-4">
          <Card className="bg-white/20 backdrop-blur-sm border-0 p-4 text-center">
            <div className="flex items-center justify-center gap-2 mb-2">
              <Award className="w-6 h-6 text-white" />
            </div>
            <div className="text-white">{unlockedCount}/{achievements.length}</div>
            <p className="text-amber-100">Desbloqueados</p>
          </Card>
          <Card className="bg-white/20 backdrop-blur-sm border-0 p-4 text-center">
            <div className="flex items-center justify-center gap-2 mb-2">
              <Sparkles className="w-6 h-6 text-white" />
            </div>
            <div className="text-white">{totalPoints}</div>
            <p className="text-amber-100">Puntos de Logros</p>
          </Card>
        </div>
      </div>

      {/* Lista de logros */}
      <div className="px-6 space-y-4">
        {achievements.map((achievement) => {
          const Icon = achievement.icon;
          const progressPercentage = (achievement.progress / achievement.maxProgress) * 100;
          
          return (
            <Card 
              key={achievement.id} 
              className={`border-0 shadow-md transition-all ${
                achievement.unlocked 
                  ? 'bg-white' 
                  : 'bg-gray-50 opacity-75'
              }`}
            >
              <div className="p-5">
                <div className="flex items-start gap-4">
                  {/* Icono del logro */}
                  <div className={`relative flex-shrink-0 w-16 h-16 rounded-xl bg-gradient-to-br ${getRarityColor(achievement.rarity)} flex items-center justify-center shadow-lg ${
                    !achievement.unlocked && 'grayscale'
                  }`}>
                    <Icon className="w-8 h-8 text-white" />
                    {achievement.unlocked && (
                      <div className="absolute -top-2 -right-2 bg-green-500 rounded-full p-1 shadow-md">
                        <Medal className="w-4 h-4 text-white" />
                      </div>
                    )}
                  </div>

                  {/* Información del logro */}
                  <div className="flex-1 min-w-0">
                    <div className="flex items-start justify-between gap-2 mb-2">
                      <div className="flex-1 min-w-0">
                        <h3 className={`mb-1 ${!achievement.unlocked && 'text-gray-600'}`}>
                          {achievement.title}
                        </h3>
                        <p className={`text-gray-600 ${!achievement.unlocked && 'text-gray-500'}`}>
                          {achievement.description}
                        </p>
                      </div>
                      <Badge className={`${getRarityBadgeColor(achievement.rarity)} border-0 flex-shrink-0`}>
                        {getRarityName(achievement.rarity)}
                      </Badge>
                    </div>

                    {/* Barra de progreso */}
                    {!achievement.unlocked && (
                      <div className="mt-3">
                        <div className="flex items-center justify-between mb-2">
                          <span className="text-gray-600">
                            Progreso: {achievement.progress}/{achievement.maxProgress}
                          </span>
                          <span className="text-green-600">
                            +{achievement.points} pts
                          </span>
                        </div>
                        <Progress value={progressPercentage} className="h-2" />
                      </div>
                    )}

                    {achievement.unlocked && (
                      <div className="mt-2 flex items-center justify-between">
                        <Badge className="bg-green-100 text-green-700 border-0">
                          ✓ Desbloqueado
                        </Badge>
                        <span className="text-green-600">
                          +{achievement.points} pts
                        </span>
                      </div>
                    )}
                  </div>
                </div>
              </div>
            </Card>
          );
        })}
      </div>

      {/* Mensaje motivacional */}
      <div className="px-6 mt-8">
        <Card className="bg-gradient-to-r from-green-50 to-emerald-50 border-0 p-5 shadow-md">
          <div className="flex items-start gap-3">
            <div className="bg-green-600 rounded-full p-2 flex-shrink-0">
              <Leaf className="w-5 h-5 text-white" />
            </div>
            <div>
              <h3 className="text-gray-800 mb-2">¡Sigue así, {userName}!</h3>
              <p className="text-gray-600">
                Cada logro desbloqueado te acerca más a ser un Guardián del Planeta. 
                ¡Continúa reciclando y desbloquea logros épicos!
              </p>
            </div>
          </div>
        </Card>
      </div>
    </div>
  );
}