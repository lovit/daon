<template>

    <md-dialog ref="sentenceDialog">
      <md-dialog-title>말뭉치 {{title}}</md-dialog-title>

      <md-dialog-content>
        <form>
          <md-layout md-column>
            <md-layout md-flex="20" v-show="isNew">
              <md-input-container>
                <label>Index</label>
                <index-select filter="sentences" name="indexName" id="indexName" v-model="index"></index-select>
              </md-input-container>
            </md-layout>
            <md-layout md-flex="20" v-show="!isNew">
              <span class="md-subheading">
                Index : {{index}}
              </span>
            </md-layout>
            <md-layout md-flex="20">
							<md-input-container>
								<label>문장</label>
								<md-textarea v-model="sentence" @input.native="analyze"></md-textarea>
                <div style="text-align: right">
                  <md-button class="md-raised md-primary" @click.native="analyze">문장 분석</md-button>
                </div>
							</md-input-container>
            </md-layout>
            <md-layout>
							<md-input-container>
								<label>분석결과</label>
								<md-textarea v-model="eojeols" rows="30" ></md-textarea>
							</md-input-container>
            </md-layout>
          </md-layout>

        </form>
      </md-dialog-content>

      <md-dialog-actions>
        <md-button class="md-primary" @click.native="closeDialog('sentenceDialog')">취소</md-button>
        <md-button class="md-primary" @click.native="save()">저장</md-button>
      </md-dialog-actions>
    </md-dialog>

</template>



<script>
  export default {
    name: 'sentenceForm',
    props: {},
    data : function(){
      return {
        title:'신규',
        id:'',
        index:'',
        sentence:'',
        eojeols:'',
        isNew:true
      }
    },
    methods : {
      analyze: function (e) {
        let vm = this;

        if(e.target.value){
          vm.sentence = e.target.value;
        }

        if(!vm.sentence){
          return;
        }

        this.$http.post('/v1/analyze/text', vm.sentence)
          .then(function(response) {
            vm.eojeols = vm.toStringAnalyzed(response.data);
          })
      },
      openDialog: function(id, index, ref='sentenceDialog') {

      	let vm = this;

      	if(id && index){
      		let params = {id : id, index: index};
      		vm.id = id;
      		vm.index = index;
      		vm.title = '수정';
      		vm.isNew = false;
          this.$http.get('/v1/sentence/get', {params : params})
            .then(function(response) {

            	let sentence = response.body;
            	vm.sentence = sentence.sentence;
            	vm.eojeols = vm.toString(sentence.eojeols);

//            	console.log(response);
              vm.$refs[ref].open();
            })
        }else{
          vm.title = '신규';
          vm.id = '';
          vm.index = '';
          vm.sentence = '';
          vm.eojeols = '';
          vm.isNew = true;
          vm.$refs[ref].open();
        }
      },

      toStringAnalyzed: function(analyzed){
//        console.log(JSON.stringify(analyzed, null, 2));

        let arr = [];
        analyzed.forEach(function(e){
          let str = e.surface;

          let morph = [];
          e.terms.map(x=>x.keywords).flatMap(x=>x).forEach(function(m){
            morph.push(m.word + "/" + m.tag);
          });

          str += " - " + morph.join(" ");

          arr.push(str);

        });
        return arr.join('\n')
      },
      toString: function(eojeols){
//        console.log(JSON.stringify(eojeols, null, 2));

        /**
         * 어절 구분 : 줄바꿈 문자
         * 어절-형태소 간 구분 : ' - '
         * 형태소 간 구분 : 공백(스페이스) 문자
         * 형태소 내 단어-태그 구분 : '/'
         * @type {Array}
         */
        let arr = [];
        eojeols.forEach(function(e){
          let str = e.surface;

          let morph = [];
          e.morphemes.forEach(function(m){
            morph.push(m.word + "/" + m.tag);
          });

          str += " - " + morph.join(" ");

          arr.push(str);
        });

        return arr.join('\n')
      },
      closeDialog: function(ref='sentenceDialog') {
        this.$refs[ref].close();
      },

      save: function(){
        let vm = this;
        let params = {
          id: vm.id,
          index: vm.index,
          sentence: vm.sentence,
          eojeols: vm.eojeols
        };

        this.$http.post('/v1/sentence/save', params)
          .then(function(response) {
//            console.log(response);
            vm.closeDialog('sentenceDialog');
          })
      }

    }


  }
</script>

<style lang="scss">

  .md-textarea { height: 300px; }
</style>
